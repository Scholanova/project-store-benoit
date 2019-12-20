package com.scholanova.projectstore.repositories;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Store;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class StoreRepository {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public StoreRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Store getById(Integer id) throws ModelNotFoundException {
		String query = "SELECT STORES.ID as id, " +
				"STORES.NAME AS name " +
				"isnull(SUM(STOCKS.VALUE),0) AS value " +
				"FROM STORES " + 
				"LEFT JOIN STOCKS ON STOCKS.STORE_ID = STORES.ID " +
				"WHERE STORES.ID = :id " +
				"GROUP BY STORES.ID,STORES.NAME";

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("id", id);

		return jdbcTemplate.query(query,
				parameters,
				new BeanPropertyRowMapper<>(Store.class))
				.stream()
				.findFirst()
				.orElseThrow(ModelNotFoundException::new);
	}

	public Store create(Store storeToCreate) {
		KeyHolder holder = new GeneratedKeyHolder();

		String query = "INSERT INTO STORES " +
				"(NAME) VALUES " +
				"(:name)";

		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("name", storeToCreate.getName());

		jdbcTemplate.update(query, parameters, holder);

		Integer newlyCreatedId = (Integer) holder.getKeys().get("ID");
		try {
			return this.getById(newlyCreatedId);
		} catch (ModelNotFoundException e) {
			return null;
		}
	}

	public void delete(Integer id) throws ModelNotFoundException{

		String query = "DELETE FROM STORES " + 
				"WHERE ID = :id";

		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("id", id);
		int nbLinesModified = jdbcTemplate.update(query, parameters);
		if(nbLinesModified == 0) {
			throw new ModelNotFoundException();
		}
	}
	
	public void update(Store store) throws ModelNotFoundException{

		String query = "UPDATE STORES " + 
				"SET NAME = :newName " +
				"WHERE ID = :id";

		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("id", store.getId())
				.addValue("newName", store.getName());
		int nbLinesModified = jdbcTemplate.update(query, parameters);
		if(nbLinesModified == 0) {
			throw new ModelNotFoundException();
		}
	}
}
