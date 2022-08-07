package com.kitap.kitap.api;

import com.kitap.kitap.service.KitapService;

import com.kitap.kitap.domain.Kitap;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//TODO:LOGIC SHOULD BE HANDLED AT BACK; HANDLE ALL EXCEPTIONS ??;
@RestController
@RequestMapping("/kitap")
@RequiredArgsConstructor
public class KitapController {
    private final KitapService kitapService;

    @GetMapping("/all")
    public List<Kitap> getMappingKitap() {
        return kitapService.getKitap();
    }

    @GetMapping(path = "{id}")
    public Optional<Kitap> getKitapById(@PathVariable("id") Long id) {
        return kitapService.getKitapById(id);}
    @GetMapping
    public void getKitapById() {
        throw new RuntimeException("Please specify the ID of book to be drawn. ");
    }

    @PostMapping
    public Kitap postMappingKitap(@RequestBody Kitap kitap) {
        return kitapService.postKitap(kitap);
    }

    @DeleteMapping(path = "{kitap_id}")
    public void deleteMappingKitap(@PathVariable("kitap_id") Long kitap_no) {
        kitapService.deleteKitap(kitap_no);
    }
    @DeleteMapping
    public void deleteMappingKitap() {
        throw new RuntimeException("Please specify the ID of book to be deleted. ");
    }
    @PutMapping
    public Kitap putMappingKitap(@RequestBody Kitap kitap) {
        if (kitap.getId()==null){
            throw new RuntimeException("ID is not entered");
        }
        return kitapService.putKitap(kitap);
    }
    @PutMapping(path = "{id}")
    public Kitap Put(@PathVariable("id") Long id, @RequestBody Kitap kitap) {
        if (kitap.getId()==null){
            kitap.setId(id);
            return kitapService.putKitap(kitap);
        } else if(!kitap.getId().equals(id)){
            throw new RuntimeException("Path variable and the book that is entered are not the same");
        }
        return kitapService.putKitap(kitap);
    }
}