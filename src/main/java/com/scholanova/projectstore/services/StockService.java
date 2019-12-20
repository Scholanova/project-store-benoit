package com.scholanova.projectstore.services;

import java.awt.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.scholanova.projectstore.exceptions.ModelNotFoundException;
import com.scholanova.projectstore.exceptions.StockNameCannotBeEmptyException;
import com.scholanova.projectstore.exceptions.StockStoreUnvalidException;
import com.scholanova.projectstore.exceptions.StockTypeIsUnValidException;
import com.scholanova.projectstore.exceptions.StockValueInferiorThan0Exception;
import com.scholanova.projectstore.models.Stock;
import com.scholanova.projectstore.repositories.StockRepository;

@Service
public class StockService {
	private StockRepository stockRepository;
	private StoreService storeService;
	public StockService(StockRepository stockRepository,StoreService storeService) {
        this.stockRepository = stockRepository;
        this.storeService = storeService;
    }

    public Stock create(Stock stock) throws StockNameCannotBeEmptyException, StockTypeIsUnValidException, StockValueInferiorThan0Exception, StockStoreUnvalidException  {

        if (isNameMissing(stock)) {
            throw new StockNameCannotBeEmptyException();
        }
        if (isTypeUnValid(stock)) {
            throw new StockTypeIsUnValidException();
        }
        if (isValueInferiorThan0(stock)) {
            throw new StockValueInferiorThan0Exception();
        }
        if (isStoreInvalid(stock)) {
            throw new StockStoreUnvalidException();
        }
        return stockRepository.create(stock);
    }

	private boolean isStoreInvalid(Stock stock) {
		try {
			storeService.getById(stock.getStore_id());
		} catch (ModelNotFoundException e) {
			// TODO Auto-generated catch block
			return true;
		}
		return false;
	}

	private boolean isValueInferiorThan0(Stock stock) {
		return stock.getValue() == null ||
				stock.getValue()< 1;
	}

	private boolean isTypeUnValid(Stock stock) {
		if (stock.getType() == null) {
			return true;
		}
		if ( stock.getType().matches("Fruit") || stock.getType().matches("Nail")) {
			return false;
		}
		return true;
	}

	private boolean isNameMissing(Stock stock) {
		return stock.getName() == null ||
				stock.getName().trim().length() == 0;
	}
	
	public void delete(Integer store_id,Integer stock_id ) throws ModelNotFoundException {
		stockRepository.delete(store_id,stock_id);
	}

	public ArrayList getAllByStoreId(Integer store_id) {
		return stockRepository.getAllByStoreId(store_id);
	}
}
