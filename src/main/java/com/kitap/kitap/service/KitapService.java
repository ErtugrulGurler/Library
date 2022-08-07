package com.kitap.kitap.service;

import com.kitap.kitap.domain.Kitap;

import java.util.List;
import java.util.Optional;

public interface KitapService {
    List<Kitap> getKitap();
    Optional<Kitap> getKitapById(Long id);
    Kitap postKitap(Kitap kitap);
    void deleteKitap(Long kitap_no);
    Kitap putKitap(Kitap updateKitap);
    void checkByID(Long kitap_ID);
}
