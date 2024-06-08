package net.unicornuniversity.bdij.entities;

import jakarta.persistence.*;
import net.unicornuniversity.bdij.models.IcoData;

@Entity
@Table(name = "ico_data")
public class IcoDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ico;
    private String obchodniJmeno;
    private String kodStatu;
    private String nazevStatu;
    private String pravniForma;
    private String financniUrad;
    private String datumVzniku;
    private String datumAktualizace;
    private String icoId;
    private String radekAdresy1;
    private String radekAdresy2;
    private String radekAdresy3;
    private String stavZdrojeVr;
    private String primarniZdroj;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getObchodniJmeno() {
        return obchodniJmeno;
    }

    public void setObchodniJmeno(String obchodniJmeno) {
        this.obchodniJmeno = obchodniJmeno;
    }

    public String getKodStatu() {
        return kodStatu;
    }

    public void setKodStatu(String kodStatu) {
        this.kodStatu = kodStatu;
    }

    public String getNazevStatu() {
        return nazevStatu;
    }

    public void setNazevStatu(String nazevStatu) {
        this.nazevStatu = nazevStatu;
    }

    public String getPravniForma() {
        return pravniForma;
    }

    public void setPravniForma(String pravniForma) {
        this.pravniForma = pravniForma;
    }

    public String getFinancniUrad() {
        return financniUrad;
    }

    public void setFinancniUrad(String financniUrad) {
        this.financniUrad = financniUrad;
    }

    public String getDatumVzniku() {
        return datumVzniku;
    }

    public void setDatumVzniku(String datumVzniku) {
        this.datumVzniku = datumVzniku;
    }

    public String getDatumAktualizace() {
        return datumAktualizace;
    }

    public void setDatumAktualizace(String datumAktualizace) {
        this.datumAktualizace = datumAktualizace;
    }

    public String getIcoId() {
        return icoId;
    }

    public void setIcoId(String icoId) {
        this.icoId = icoId;
    }

    public String getRadekAdresy1() {
        return radekAdresy1;
    }

    public void setRadekAdresy1(String radekAdresy1) {
        this.radekAdresy1 = radekAdresy1;
    }

    public String getRadekAdresy2() {
        return radekAdresy2;
    }

    public void setRadekAdresy2(String radekAdresy2) {
        this.radekAdresy2 = radekAdresy2;
    }

    public String getRadekAdresy3() {
        return radekAdresy3;
    }

    public void setRadekAdresy3(String radekAdresy3) {
        this.radekAdresy3 = radekAdresy3;
    }

    public String getStavZdrojeVr() {
        return stavZdrojeVr;
    }

    public void setStavZdrojeVr(String stavZdrojeVr) {
        this.stavZdrojeVr = stavZdrojeVr;
    }

    public String getPrimarniZdroj() {
        return primarniZdroj;
    }

    public void setPrimarniZdroj(String primarniZdroj) {
        this.primarniZdroj = primarniZdroj;
    }

    public static IcoDataEntity from(IcoData icoData)
    {
        IcoDataEntity entity = new IcoDataEntity();

        entity.setIco(icoData.getIco());
        entity.setObchodniJmeno(icoData.getObchodniJmeno());
        entity.setPravniForma(icoData.getPravniForma());
        entity.setFinancniUrad(icoData.getFinancniUrad());
        entity.setDatumVzniku(icoData.getDatumVzniku());
        entity.setDatumAktualizace(icoData.getDatumAktualizace());
        entity.setIcoId(icoData.getIcoId());
        entity.setPrimarniZdroj(icoData.getPrimarniZdroj());

        if (icoData.getAdresaDorucovaci() != null) {
            entity.setRadekAdresy1(icoData.getAdresaDorucovaci().getRadekAdresy1());
            entity.setRadekAdresy2(icoData.getAdresaDorucovaci().getRadekAdresy2());
            entity.setRadekAdresy3(icoData.getAdresaDorucovaci().getRadekAdresy3());
        }

        if (icoData.getSidlo() != null) {
            entity.setKodStatu(icoData.getSidlo().getKodStatu());
            entity.setNazevStatu(icoData.getSidlo().getNazevStatu());
        }

        return entity;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }
}

