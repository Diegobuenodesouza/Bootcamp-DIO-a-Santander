package com.project.bootcamp.service;

import com.project.bootcamp.exceptions.BusinessException;
import com.project.bootcamp.exceptions.NotFoundException;
import com.project.bootcamp.mapper.StockMapper;
import com.project.bootcamp.model.Stock;
import com.project.bootcamp.model.dto.StockDTO;
import com.project.bootcamp.repository.StockRepository;
import com.project.bootcamp.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;



    @Transactional
    public StockDTO save(StockDTO dto) {

        Optional<Stock> optionalStock = stockRepository.findByNameAndDate(dto.getName(), dto.getDate());

        if (optionalStock.isPresent()){
            throw  new BusinessException(MessageUtil.STOCK_ALREADY_EXISTS);
        }

        Stock stock = stockMapper.toEntity(dto);
        stockRepository.save(stock);
        return  stockMapper.toDto(stock);
    }

    @Transactional
    public StockDTO update(StockDTO dto) {

        Optional<Stock> optionalStock = stockRepository.findByStockUpdate(dto.getName(), dto.getDate(), dto.getId());

        if (optionalStock.isPresent()){
            throw  new BusinessException(MessageUtil.STOCK_ALREADY_EXISTS);
        }
        Stock stock = stockMapper.toEntity(dto);
        stockRepository.save(stock);
        return  stockMapper.toDto(stock);
    }

    @Transactional(readOnly = true)
    public List<StockDTO> findAll() {
        List<Stock> list = stockRepository.findAll();
        return stockMapper.toDtoList(list);
    }

    @Transactional(readOnly = true)
    public StockDTO getStockById(Long id) {
        return stockRepository.findById(id).map(stockMapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public StockDTO deleteById(Long id) {
        StockDTO dto = this.getStockById(id);
        stockRepository.deleteById(dto.getId());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<StockDTO> findByToday() {
        return stockRepository.findByToday(LocalDate.now())
                .map(stockMapper::toDtoList).orElseThrow(NotFoundException::new);
    }
}
