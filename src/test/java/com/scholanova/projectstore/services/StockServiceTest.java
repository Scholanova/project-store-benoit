package com.scholanova.projectstore.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.StockNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.StockStoreUnvalidException;
import com.scholanova.projectstore.exceptions.StockTypeIsUnValidException;
import com.scholanova.projectstore.exceptions.StockValueInferiorThan0Exception;
import com.scholanova.projectstore.exceptions.StoreNameCannotBeEmptyException;
import com.scholanova.projectstore.models.Stock;
import com.scholanova.projectstore.models.Store;
import com.scholanova.projectstore.repositories.StockRepository;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

	private StockService stockService;

	@Mock
	private StockRepository stockRepository;
	@Mock
	private StoreService storeService;
	
	@BeforeEach
	void setUp() {
		stockService = new StockService(stockRepository,storeService);
	}
	
	@Test
	void givenNoStockName_whenCreated_failsWithStockNameCannotBeEmptyError() {
		// GIVEN
		String name = null;
		String type = "fruit";
		Integer value = 10;
		
		Stock emptyNameStock = new Stock(name, type, value);

		// WHEN
		assertThrows(StockNameCannotBeEmptyException.class, () -> {
			stockService.create(emptyNameStock);
		});

		// THEN
		verify(stockRepository, never()).create(emptyNameStock);
	}
	@Test
	void givenBadType_whenCreated_failsWithStockTypeIsUnValidExceptionError() {
		// GIVEN
		String name = "OKLol";
		String type = null;
		Integer value = 10;
		
		Stock emptyTypeStock = new Stock(name, type, value);

		// WHEN
		assertThrows(StockTypeIsUnValidException.class, () -> {
			stockService.create(emptyTypeStock);
		});

		// THEN
		verify(stockRepository, never()).create(emptyTypeStock);
	}
	
	@Test
	void givenBadValue_whenCreated_failsWithStockValueInferiorThan0ExceptionError() {
		// GIVEN
		String name = "OKLol";
		String type = "Fruit";
		Integer value = null;
		
		Stock emptyValuStock = new Stock(name, type, value);

		// WHEN
		assertThrows(StockValueInferiorThan0Exception.class, () -> {
			stockService.create(emptyValuStock);
		});

		// THEN
		verify(stockRepository, never()).create(emptyValuStock);
	}
	
	@Test
	void givenBadStoreId_whenCreated_failsWithStockStoreUnvalidExceptionError() throws ModelNotFoundException {
		// GIVEN
		String name = "OKLol";
		String type = "Fruit";
		Integer value = 1;
		
		Integer store_id = 999;
		
		Stock badStoreIdStock = new Stock(name, type, value);
		
		badStoreIdStock.setStore_id(store_id);
		
		when(storeService.getById(store_id)).thenThrow(new ModelNotFoundException());
		// WHEN
		assertThrows(StockStoreUnvalidException.class, () -> {
			stockService.create(badStoreIdStock);
		});

		// THEN
		verify(stockRepository, never()).create(badStoreIdStock);
	}
	
	@Test
	void givenCorrectStore_whenCreated_savesStoreInRepository() throws Exception {
		// GIVEN
		Integer id = 1;
		String name = "OKLol";
		String type = "Fruit";
		Integer value = 1;
		
		Integer store_id = 999;
		
		Stock correctStock = new Stock(name,type,value);
		correctStock.setStore_id(store_id);
		Stock savedStock = new Stock(id,name, type, value,store_id);
		
		// WHEN
		Store storeReturned = new Store(store_id,"Store");
		when(storeService.getById(store_id)).thenReturn(storeReturned);

		when(stockRepository.create(correctStock)).thenReturn(savedStock);
		Stock returnedStock = stockService.create(correctStock);
		// THEN
		verify(stockRepository, atLeastOnce()).create(correctStock);
		assertThat(returnedStock).isEqualTo(savedStock);
	}

}
