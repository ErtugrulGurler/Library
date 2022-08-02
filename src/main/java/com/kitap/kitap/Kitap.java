package com.kitap.kitap;
import javax.persistence.*;

@Entity
@Table
public class Kitap {
    @Id
    @SequenceGenerator(
            name ="sequence",
            sequenceName = "sequence",
            allocationSize = 1
    )
    @GeneratedValue(
        strategy =GenerationType.SEQUENCE,
        generator = "sequence"
    )
    private Long id;
    private String adı;
    private String yazarı;
    private Integer sayfa_sayısı;

    public Kitap(Long id, String adı, String yazarı, Integer sayfa_sayısı) {
        this.id = id;
        this.adı = adı;
        this.yazarı = yazarı;
        this.sayfa_sayısı = sayfa_sayısı;
    }


    public Kitap(String adı, String yazarı, Integer sayfa_sayısı) {
        this.adı = adı;
        this.yazarı = yazarı;
        this.sayfa_sayısı = sayfa_sayısı;
    }
    public Kitap() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdı() {
        return adı;
    }

    public void setAdı(String adı) {
        this.adı = adı;
    }

    public String getYazarı() {
        return yazarı;
    }

    public void setYazarı(String yazarı) {
        this.yazarı = yazarı;
    }

    public Integer getSayfa_sayısı() {
        return sayfa_sayısı;
    }

    public void setSayfa_sayısı(Integer sayfa_sayısı) {
        this.sayfa_sayısı = sayfa_sayısı;
    }
}
