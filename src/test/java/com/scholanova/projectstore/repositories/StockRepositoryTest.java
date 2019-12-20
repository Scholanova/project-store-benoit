package com.scholanova.projectstore.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.models.Stock;
import com.scholanova.projectstore.models.Store;

@SpringJUnitConfig(StockRepository.class)
@JdbcTest
public class StockRepositoryTest {
	 	@Autowired
	    private StockRepository stockRepository;

	    @Autowired
	    private JdbcTemplate jdbcTemplate;

	    @AfterEach
	    void cleanUp() {
	        JdbcTestUtils.deleteFromTables(jdbcTemplate, "STOCKS");
	    }
	    @Nested
	    class Test_getById {

	        @Test
	        void whenNoStoresWithThatId_thenThrowsException() throws Exception {
	            // Given
	            Integer id = 1000;

	            // When & Then
	            assertThrows(ModelNotFoundException.class, () -> {
	            	stockRepository.getById(200,id);
	            });
	        }

	        @Test
	        void whenStoreExists_thenReturnsTheStore() throws Exception {
	            // Given
	            Integer id = 1;
	            Stock stock = new Stock(id, "Carrefour","Fruit",125,10);
	            insertStock(stock);

	            // When
	            Stock extractedStock = stockRepository.getById(stock.getId(),id);

	            // Then

	            System.out.println("test");
	            System.out.println(extractedStock);
	            assertThat(extractedStock).isEqualToComparingFieldByField(stock);
	        }
	    }
	    
	    @Nested
	    class Test_create {

	        @Test
	        void whenCreateStore_thenStoreIsInDatabaseWithId() {
	            // Given
	        	Integer id = 1;
	            String storeName = "Auchan";
	            String type = "AuchanLivre";
	            Integer value = 2;
	            Integer store_id = 2;
	            Stock stockToCreate = new Stock(id, storeName,type,value,store_id);

	            // When
	            Stock createdStock = stockRepository.create(stockToCreate);

	            // Then
	            assertThat(createdStock.getId()).isNotNull();
	            assertThat(createdStock.getName()).isEqualTo(storeName);
	        }
	    }
	    
	    private void insertStock(Stock stock) {
	        String query = "INSERT INTO STOCKS " +
	                "(ID, NAME,TYPE,VALUE,STORE_ID) " +
	                "VALUES ('%d', '%s', '%s', '%d', '%d')";
	        jdbcTemplate.execute(
	                String.format(query, stock.getId(), stock.getName(), stock.getType(),stock.getValue(), stock.getStore_id()));
	    }
}
	