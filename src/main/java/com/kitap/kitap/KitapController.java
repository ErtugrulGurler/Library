package com.kitap.kitap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/kitap")

public class KitapController {
    private final KitapService kitapService;
    private final KitapRepository kitapRepository;
    @Autowired
    public KitapController(KitapService kitapService , KitapRepository kitapRepository) {
        this.kitapService = kitapService;
        this.kitapRepository = kitapRepository;
    }

    @GetMapping
    public List<Kitap> getMappingKitap() {
        return kitapService.getKitap();
    }

    @GetMapping(path = "{id}")
    public Optional<Kitap> getKitapById(@PathVariable("id") Long id) {
        return kitapService.getKitapById(id);
    }

    @PostMapping
    public Kitap postMappingKitap(@RequestBody Kitap kitap) {
        return kitapService.postKitap(kitap);
    }

    @DeleteMapping(path = "{kitap_id}")
    public void deleteMappingKitap(@PathVariable("kitap_id") Long kitap_no) {
        kitapService.deleteKitap(kitap_no);
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
        } else if(kitap.getId()!= id){
            throw new RuntimeException("Path variable and the book that is entered are not the same");
        }
        return kitapService.putKitap(kitap);
    }
}