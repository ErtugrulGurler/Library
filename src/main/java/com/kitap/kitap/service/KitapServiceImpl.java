package com.kitap.kitap.service;


import com.kitap.kitap.Repo.KitapRepository;
import com.kitap.kitap.domain.Kitap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KitapServiceImpl implements KitapService{
    private final KitapRepository kitapRepository;

    @Autowired
    public KitapServiceImpl(KitapRepository kitapRepository) {
        this.kitapRepository = kitapRepository;
    }
    @Override
    public List<Kitap> getKitap() {
        if (kitapRepository.findAll().isEmpty()){throw new RuntimeException("There is no book to show. ");}
        return kitapRepository.findAll();
    }
    @Override
    public Optional<Kitap> getKitapById(Long id) {
        checkByID(id);
        return kitapRepository.findById(id);
    }
    @Override
    public Kitap postKitap(Kitap kitap) {
        if (kitap.getAdı()==null||kitap.getYazarı()==null){throw new RuntimeException("Book author or name should not be empty. ");}
        kitapRepository.save(kitap);
        return kitapRepository.getById(kitap.getId());

    }
    @Override
    public void deleteKitap(Long kitap_no) {
        checkByID(kitap_no);
        kitapRepository.deleteById(kitap_no);
    }
    @Override
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
    @Override
    public void checkByID(Long kitap_ID) {
        boolean exists = kitapRepository.existsById(kitap_ID);
        if (!exists) {
            throw new IllegalStateException("The Book with ID that is entered does not exist. ");
        }
    }
}
