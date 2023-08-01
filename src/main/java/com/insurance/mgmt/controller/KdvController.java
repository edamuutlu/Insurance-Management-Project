package com.insurance.mgmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insurance.mgmt.entity.Kdv;
import com.insurance.mgmt.service.KdvService;

@RestController
@RequestMapping("/api/kdv")
public class KdvController {
    
    @Autowired
    private KdvService kdvService;

    @PutMapping("/updateCarKdvRate")
    public ResponseEntity<Kdv> updateCarKdvRate(@RequestParam int kdvRate) {
        Kdv carKdv = kdvService.getProductTypeById(1);
        carKdv.setKdvRate(kdvRate);
        kdvService.save(carKdv);
        return ResponseEntity.ok(carKdv);
    }

    @PutMapping("/updateHomeKdvRate")
    public ResponseEntity<Kdv> updateHomeKdvRate(@RequestParam int kdvRate) {
        Kdv homeKdv = kdvService.getProductTypeById(2);
        homeKdv.setKdvRate(kdvRate);
        kdvService.save(homeKdv);
        return ResponseEntity.ok(homeKdv);
    }
}
