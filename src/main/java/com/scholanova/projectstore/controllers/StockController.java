package com.scholanova.projectstore.controllers;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.scholanova.projectstore.exceptions.StockNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.StockStoreUnvalidException;
import com.scholanova.projectstore.exceptions.StockTypeIsUnValidException;
import com.scholanova.projectstore.exceptions.StockValueInferiorThan0Exception;
import com.scholanova.projectstore.models.Stock;
import com.scholanova.projectstore.services.StockService;

@RestController
public class StockController {

	private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
    
    @PostMapping(path = "/stores/{store_id}/stocks")
    public ResponseEntity<?> createStore(@PathVariable("store_id") Integer store_id,@RequestBody Stock stock) throws StockNameCannotBeEmptyException, StockTypeIsUnValidException, StockValueInferiorThan0Exception, StockStoreUnvalidException {
        try{
        	stock.setStore_id(store_id);
        	Stock createdStore = stockService.create(stock);
        	return ResponseEntity.ok()
        			.body(createdStore);
        }catch(StockNameCannotBeEmptyException e) {
        	HashMap<String, String> returnBody = new HashMap<String, String>();
        	returnBody.put("message", "Name cannot be empty");
        	return ResponseEntity.badRequest().body(returnBody);
        }
    }
}
