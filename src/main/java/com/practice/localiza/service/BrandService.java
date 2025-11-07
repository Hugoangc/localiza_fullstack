package com.practice.localiza.service;

import com.practice.localiza.entity.Brand;
import com.practice.localiza.entity.Car;
import com.practice.localiza.exception.ResourceNotFoundException;
import com.practice.localiza.repository.BrandRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> findAll(){
        return this.brandRepository.findAll();
    }

    public Brand findById(Long id) {
        return this.brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", id));
    }

    public List<Brand> findByName(String name) {
        return this.brandRepository.findByNameContainingIgnoreCase(name);
    }

    public Brand save(Brand brand) {
        return this.brandRepository.save(brand);
    }

    public Brand update(Long id, Brand brand) {
        Brand existingBrand = this.findById(id);
        existingBrand.setName(brand.getName());
        return this.brandRepository.save(existingBrand); }

    public void deleteById(Long id) {
        this.brandRepository.deleteById(id);
    }

}