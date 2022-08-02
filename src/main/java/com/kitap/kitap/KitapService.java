package com.kitap.kitap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KitapService {
    private final KitapRepository kitapRepository;

    @Autowired
    public KitapService(KitapRepository kitapRepository) {
        this.kitapRepository = kitapRepository;
    }

    public List<Kitap> getKitap() {

        return kitapRepository.findAll();
    }

    public Optional<Kitap> getKitapById(Long id) {
        checkByID(id);
        return kitapRepository.findById(id);
    }

    public Kitap postKitap(Kitap kitap) {

        kitapRepository.save(kitap);
        return kitapRepository.getById(kitap.getId());

    }

    public void deleteKitap(Long kitap_no) {

        checkByID(kitap_no);
        kitapRepository.deleteById(kitap_no);
    }

    public Kitap putKitap(Kitap updateKitap) {
        checkByID(updateKitap.getId());
        Optional<Kitap> kitapFind = kitapRepository.findById(updateKitap.getId());
        Kitap dbkitap = kitapRepository.getById(updateKitap.getId());

        if (kitapFind.isPresent()) {
            if (updateKitap.getAdı() == null) {
                updateKitap.setAdı(dbkitap.getAdı());
            } else {
                dbkitap.setAdı(updateKitap.getAdı());
            }
            if (updateKitap.getYazarı() == null) {
                updateKitap.setYazarı(dbkitap.getYazarı());
            } else {
                dbkitap.setYazarı(updateKitap.getYazarı());
            }
            if (updateKitap.getSayfa_sayısı() == null) {
                updateKitap.setSayfa_sayısı(dbkitap.getSayfa_sayısı());
            } else {
                dbkitap.setSayfa_sayısı(updateKitap.getSayfa_sayısı());
            }
        }

        dbkitap = updateKitap;
        kitapRepository.save(dbkitap);
        return kitapRepository.getById(dbkitap.getId());
    }

    public void checkByID(Long kitap_ID) {
        boolean exists = kitapRepository.existsById(kitap_ID);
        if (!exists) {
            throw new IllegalStateException("The Book with ID that is entered does not exist. ");
        }
    }
}
