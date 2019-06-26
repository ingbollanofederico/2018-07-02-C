package it.polito.tdp.extflightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.extflightdelays.model.Adiacenza;
import it.polito.tdp.extflightdelays.model.Airline;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Flight;
import it.polito.tdp.extflightdelays.model.Model;

public class ExtFlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT * from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRLINE")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports(Map<Integer, Airport> idMapAirport) {
		String sql = "SELECT * FROM airports";
		List<Airport> result = new ArrayList<Airport>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(idMapAirport.get(rs.getInt("ID"))==null) {
				
				Airport airport = new Airport(rs.getInt("ID"), rs.getString("IATA_CODE"), rs.getString("AIRPORT"),
						rs.getString("CITY"), rs.getString("STATE"), rs.getString("COUNTRY"), rs.getDouble("LATITUDE"),
						rs.getDouble("LONGITUDE"), rs.getDouble("TIMEZONE_OFFSET"));
				idMapAirport.put(rs.getInt("ID"), airport);
				result.add(airport);
				}
				else {
					result.add(idMapAirport.get(rs.getInt("ID")));
				}
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT * FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("ID"), rs.getInt("AIRLINE_ID"), rs.getInt("FLIGHT_NUMBER"),
						rs.getString("TAIL_NUMBER"), rs.getInt("ORIGIN_AIRPORT_ID"),
						rs.getInt("DESTINATION_AIRPORT_ID"),
						rs.getTimestamp("SCHEDULED_DEPARTURE_DATE").toLocalDateTime(), rs.getDouble("DEPARTURE_DELAY"),
						rs.getDouble("ELAPSED_TIME"), rs.getInt("DISTANCE"),
						rs.getTimestamp("ARRIVAL_DATE").toLocalDateTime(), rs.getDouble("ARRIVAL_DELAY"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public void loadVertexOrigin( Map<Integer, Airport> idMapAirport,
			Map<Airport, Integer> mapAirportCorrect, Integer compagnie) {
		
		String sql = "SELECT ORIGIN_AIRPORT_ID as id, COUNT(DISTINCT AIRLINE_ID ) as count " + 
				" FROM flights " + 
				" GROUP BY `ORIGIN_AIRPORT_ID` " + 
				" HAVING COUNT(DISTINCT AIRLINE_ID) >?  ";
		

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, compagnie);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				//aggiungo gli aeroporti in partenza con tot numero di compagnie
				mapAirportCorrect.put(idMapAirport.get(rs.getInt("id")), rs.getInt("count"));
				
			}

			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public void loadVertexDestination(Map<Integer, Airport> idMapAirport,
			Map<Airport, Integer> mapAirportCorrect, Integer compagnie) {
		
			String sql = "SELECT DESTINATION_AIRPORT_ID as id, COUNT(DISTINCT AIRLINE_ID ) as count " + 
					" FROM flights " + 
					" GROUP BY `DESTINATION_AIRPORT_ID` " + 
					" HAVING COUNT(DISTINCT AIRLINE_ID )>? ";
			

			try {
				Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, compagnie);
				ResultSet rs = st.executeQuery();

				while (rs.next()) {
					//aggiungo gli aeroporti in arrivo con tot numero di compagnie solo se non già presentini in 
					if(mapAirportCorrect.get(idMapAirport.get(rs.getInt("id")))==null){
						mapAirportCorrect.put(idMapAirport.get(rs.getInt("id")), rs.getInt("count"));
					}
			
				}

				conn.close();
				

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Errore connessione al database");
				throw new RuntimeException("Error Connection Database");
			}
		
	}

	public List<Adiacenza> loadAdiacenze(Map<Airport, Integer> mapAirportCorrect, Map<Integer, Airport> idMapAirport) {
		
		String sql = "SELECT `ORIGIN_AIRPORT_ID` AS ori, `DESTINATION_AIRPORT_ID` as des, COUNT(*) as cnt " + 
				" FROM flights " + 
				" GROUP BY `ORIGIN_AIRPORT_ID`,`DESTINATION_AIRPORT_ID` ";
		
		List<Adiacenza> list = new ArrayList<Adiacenza>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Airport origin = idMapAirport.get(rs.getInt("ori"));
				Airport destination = idMapAirport.get(rs.getInt("des"));
				
				
				if(mapAirportCorrect.containsKey(origin) && mapAirportCorrect.containsKey(destination)) {
					list.add(new Adiacenza(origin,destination, rs.getInt("cnt")));
				}
				
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
