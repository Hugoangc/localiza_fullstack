package com.practice.localiza.config;

import com.practice.localiza.entity.Acessory;
import com.practice.localiza.entity.Brand;
import com.practice.localiza.entity.Car;
import com.practice.localiza.entity.User;
import com.practice.localiza.repository.AcessoryRepository;
import com.practice.localiza.repository.BrandRepository;
import com.practice.localiza.repository.CarRepository;
import com.practice.localiza.auth.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private AcessoryRepository acessoryRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; //

    @Override
    public void run(String... args) throws Exception {
        if (brandRepository.count() > 0) {
            System.out.println("O banco de dados já está populado. Seed não executado.");
            return;
        }

        System.out.println("Populando o banco de dados com dados de teste...");

        // CRIAR ACESSÓRIOS
        Acessory acc1 = new Acessory(null, "Bancos de Couro", "Bancos de couro premium", 1500.00);
        Acessory acc2 = new Acessory( null,"Central Multimídia", "Tela de 10 polegadas com CarPlay", 2500.00);
        Acessory acc3 = new Acessory( null,"Rodas de Liga Leve", "Aro 18, design esportivo", 2000.00);

        acessoryRepository.saveAll(List.of(acc1, acc2, acc3));

        // CRIAR MARCAS
        Brand brand1 = new Brand();
        brand1.setName("Toyota");

        Brand brand2 = new Brand();
        brand2.setName("Honda");

        brandRepository.saveAll(List.of(brand1, brand2));

        // CRIAR USUÁRIOS
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRole("USER"); //
        user.setEmail("user@email.com");
        user.setName("Usuário Comum");

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN"); //
        admin.setEmail("admin@email.com");
        admin.setName("Administrador");

        loginRepository.saveAll(List.of(user, admin));

        //  CRIAR CARROS
        Car car1 = new Car();
        car1.setName("Corolla");
        car1.setColor("Prata");
        car1.setPrice(120000.00);
        car1.setManufactureYear(2023);
        car1.setCarStatus(true); // Está ativo
        car1.setStock(10);
        car1.setAccMultiplier(1.1); // Acessórios 10% mais caros neste modelo
        car1.setBrand(brand1); // Toyota
        car1.setAcessories(List.of(acc1, acc2)); // Bancos de Couro e Multimídia

        Car car2 = new Car();
        car2.setName("Civic");
        car2.setColor("Preto");
        car2.setPrice(130000.00);
        car2.setManufactureYear(2023);
        car2.setCarStatus(true);
        car2.setStock(5);
        car2.setAccMultiplier(1.0); // Preço padrão para acessórios
        car2.setBrand(brand2); // Honda
        car2.setAcessories(List.of(acc2, acc3)); // Multimídia e Rodas

        Car car3 = new Car();
        car3.setName("Fit");
        car3.setColor("Azul");
        car3.setPrice(80000.00);
        car3.setManufactureYear(2021);
        car3.setCarStatus(false); // Inativo (soft-delete)
        car3.setStock(3);
        car3.setAccMultiplier(1.0);
        car3.setBrand(brand2); // Honda
        car3.setAcessories(List.of()); // Sem acessórios

        carRepository.saveAll(List.of(car1, car2, car3));

        System.out.println("Banco de dados populado com sucesso!");
    }
}