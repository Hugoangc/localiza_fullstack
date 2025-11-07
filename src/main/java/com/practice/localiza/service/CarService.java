package com.practice.localiza.service;

import com.practice.localiza.entity.Brand;
import com.practice.localiza.entity.Car;
import com.practice.localiza.repository.CarRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public Car save(Car car) {
        return this.carRepository.save(car);
    }

    public Car findById(Long id) {
        return this.carRepository.findById(id).orElse(null);
    }

    public List<Car> findByName(String name) {
        return this.carRepository.findByName(name);
    }
    public List<Car> findNames(String name) {
        return this.carRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Car> findYearGte(Integer manufactureYear) {
        return this.carRepository.findByYearGte(manufactureYear);
    }

//    public List<Car> findByBrand(Long brandId) {
//        Brand brand = new Brand();
//        brand.setId(brandId);
//        return carRepository.findByBrand(brand);
//    }


    public List<Car> findByBrand(Long brandId) {
        return carRepository.findByBrandId(brandId);
    }
    public List<Car> findAll() {
        return this.carRepository.findAllByCarStatusTrue();
    }

    public Car update(Long id, Car newCarData) {

        Car existingCar = this.findById(id);
        existingCar.setName(newCarData.getName());
        existingCar.setColor(newCarData.getColor());
        existingCar.setPrice(newCarData.getPrice());
        existingCar.setBrand(newCarData.getBrand());
        existingCar.setManufactureYear(newCarData.getManufactureYear());
        existingCar.setCarStatus(newCarData.isCarStatus());
        existingCar.setAcessories(newCarData.getAcessories());
        return this.carRepository.save(existingCar);
    }

    public List<Car> findByPriceRange(Double minPrice, Double maxPrice) {
        return carRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Transactional
    public String softDelete(Long id) {
        Car carToInactivate = this.findById(id);
        carToInactivate.setCarStatus(false);
        this.carRepository.save(carToInactivate);
        return "Car was successfully inactivated. ID: " +  carToInactivate.getId();
    }
}
