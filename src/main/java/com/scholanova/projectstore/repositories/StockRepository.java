package com.scholanova.projectstore.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Stock;

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
			return this.getById(newlyCreatedId);
		} catch (ModelNotFoundException e) {
			return null;
		}
	}

	private Stock getById(Integer id) throws ModelNotFoundException{
		String query = "SELECT ID as id, " +
				"NAME AS name " +
				"TYPE AS type " +
				"VALUE AS value " +
				"STORE_ID AS store_id " +
				"FROM STOCKS " +
				"WHERE ID = :id";

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("id", id);
		
		return jdbcTemplate.query(query,
				parameters,
				new BeanPropertyRowMapper<>(Stock.class))
				.stream()
				.findFirst()
				.orElseThrow(ModelNotFoundException::new);
	}

}
