package com.practice.localiza.controller;

import com.practice.localiza.entity.Brand;
import com.practice.localiza.entity.Car;
import com.practice.localiza.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/car")
@CrossOrigin("*") // posso colocar "http://localhost:4200") permitindo só requisições dessa origim
public class CarController {

    @Autowired
    private CarService carService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Car car) {
        try {
            Car savedCar = carService.save(car);
            return  new ResponseEntity<>(savedCar.getId().toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error on saving",  HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByPriceRange")
    public ResponseEntity<List<Car>> findByPriceRange(@RequestParam Double min, @RequestParam Double max) {
        try {
            List<Car> list = carService.findByPriceRange(min, max);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Car>> findAll() {
        try {
            List<Car> list = carService.findAll();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> findById(@PathVariable Long id) {
        try {
            Car car = this.carService.findById(id);
            return new ResponseEntity<>(car, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> update(@PathVariable Long id, @RequestBody Car carNovosDados) {
        try {
            Car carroAtualizado = this.carService.update(id, carNovosDados);
            return new ResponseEntity<>(carroAtualizado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            String mensage = this.carService.softDelete(id);
            return new ResponseEntity<>(mensage, HttpStatus.OK);

        } catch (Exception e) {
            String errorMessage = "Error: Could not process delete request for ID " + id + "Error: "+ e;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);        }
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<Car>> findByName(@RequestParam String name) {
        try{
            List<Car> list = carService.findByName(name);
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/findNames")
    public ResponseEntity<List<Car>> findNames(@RequestParam String name) {
        try{
            List<Car> list = carService.findNames(name);
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/findByBrand")
    public ResponseEntity<List<Car>> findByBrand(@RequestParam Long brandId) {
        try{
            List<Car> list = carService.findByBrand(brandId);
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/findByYearGte")
    public ResponseEntity<List<Car>> findByYearGte(@RequestParam int manufactureYear) {
        try{
            List<Car> list = carService.findYearGte(manufactureYear);
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }
}
