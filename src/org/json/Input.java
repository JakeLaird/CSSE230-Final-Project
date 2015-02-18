package org.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rhnavigator.map.Map;
import rhnavigator.map.Map.NeighborConnection;

public class Input {
	public static void main(String[] args) {
		Map map = Map.getSample();
		// System.out.println(map.getstring());
		buildtext(map, "first", true);
		Map secondmap=output("first");
		System.out.println(secondmap.getstring());
		// 39.483861, -87.330348
		// 39.481886, -87.324785

		// ArrayList<MapPoint> result = map.findInRange(39.481886, 39.483861,
		// -87.330348, -87.324785);
		// System.out.println("All:" + result.size());
		//
		// // 39.482942, -87.325220
		//
		// result = map.findInRange(39.482942, 39.483861, -87.330348,
		// -87.324785);
		// System.out.println("Less:" + result);
	}

	public static void buildtext(Map m, String text, boolean isnew) {
		if (isnew) {
			try {
				File file = new File(text);
				file.createNewFile();
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(m.getstring());
				bw.flush();
				bw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {

				FileWriter fw = new FileWriter(text);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(m.getstring());
				bw.flush();
				bw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Map output(String text) {
		Map map = new Map();
		try {

			FileReader fr = new FileReader(text);
			BufferedReader br = new BufferedReader(fr);
			while (true) {
				String temp = br.readLine();

				if (temp != null) {
					JSONArray arr = new JSONArray(temp);
					JSONArray arrr = new JSONArray(arr.get(3).toString());
//					System.out.println(arrr.get(0).toString()
//							.substring(1, arrr.get(0).toString().length()));
					double latitude = (double) arr.get(0);
					double longitude = (double) arr.get(1);
					String name = (String) arr.get(2);
					List<NeighborConnection> list = new ArrayList<NeighborConnection>();
					for (int i = 0; i < arrr.length(); i+=2) {
						String neighborName = arrr.get(i).toString().substring(0, arrr.get(i).toString().length()).replaceAll("<|>","");
						String cost = arrr.get(i+1).toString().substring(0, arrr.get(i+1).toString().length()).replaceAll("<|>","");
						list.add(new NeighborConnection(neighborName, (int) Double.parseDouble(cost)));

					}
					List<NeighborConnection> neighbors = list;
					int interestLevel = (int) arr.get(4);
					map.addPointWithCost(latitude, longitude, name, neighbors,
							interestLevel);
				} else {
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (map.hasPendingConnections()) {
			map.processPending();
		}
		return map;
	}
}
