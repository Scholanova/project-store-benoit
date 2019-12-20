package com.scholanova.projectstore.repositories;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Stock;
@Repository
public class StockRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public StockRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Stock create(Stock stockToCreate) {
		KeyHolder holder = new GeneratedKeyHolder();

		String query = "INSERT INTO STOCKS " +
                "(NAME, TYPE, VALUE, STORE_ID) VALUES " +
                "(:name, :type, :value, :store_id)";

		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("name", stockToCreate.getName())
				.addValue("type", stockToCreate.getType())
				.addValue("value", stockToCreate.getValue())
				.addValue("store_id", stockToCreate.getStore_id());
		jdbcTemplate.update(query, parameters, holder);

		Integer newlyCreatedId = (Integer) holder.getKeys().get("ID");
		try {
			return this.getById(stockToCreate.getStore_id(),newlyCreatedId);
		} catch (ModelNotFoundException e) {
			return null;
		}
	}

	public Stock getById(Integer store_id,Integer stock_id) throws ModelNotFoundException{
		String query = "SELECT ID as id, " +
				"NAME AS name, " +
				"TYPE AS type, " +
				"VALUE AS value, " +
				"STORE_ID AS store_id " +
				"FROM STOCKS " +
				"WHERE ID = :stock_id" +
				" AND STORE_ID = :store_id";
				
		SqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("store_id", store_id)
			.addValue("stock_id", stock_id);
		
		return jdbcTemplate.query(query,
				parameters,
				new BeanPropertyRowMapper<>(Stock.class))
				.stream()
				.findFirst()
				.orElseThrow(ModelNotFoundException::new);
	}
	
	public void delete(Integer store_id,Integer stock_id) throws ModelNotFoundException{

		String query = "DELETE FROM STOCKS " + 
				"WHERE ID = :stock_id" +
				" AND STORE_ID = :store_id";

		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("store_id", store_id)
				.addValue("stock_id", stock_id);
		int nbLinesModified = jdbcTemplate.update(query, parameters);
		if(nbLinesModified == 0) {
			throw new ModelNotFoundException();
		}
	}

	public ArrayList getAllByStoreId(Integer store_id) {
		String query = "SELECT ID as id, " +
				"NAME AS name, " +
				"TYPE AS type, " +
				"VALUE AS value, " +
				"STORE_ID AS store_id " +
				"FROM STOCKS " +
				"WHERE STORE_ID = :store_id";
				
		SqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("store_id", store_id);
		
		return jdbcTemplate.query(query,
				parameters,
				new BeanPropertyRowMapper<>(Stock.class))
				.stream()
				.collect(Collectors.toCollection(ArrayList::new));
	}

}
