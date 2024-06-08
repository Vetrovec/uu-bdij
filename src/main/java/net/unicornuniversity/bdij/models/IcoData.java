package net.unicornuniversity.bdij.models;

import java.util.List;

public class IcoData {
    private String ico;
    private String obchodniJmeno;
    private Sidlo sidlo;
    private String pravniForma;
    private String financniUrad;
    private String datumVzniku;
    private String datumAktualizace;
    private String icoId;
    private AdresaDorucovaci adresaDorucovaci;
    private SeznamRegistraci seznamRegistraci;
    private String primarniZdroj;
    private List<String> czNace;

    public String getIco() { return ico; }
    public void setIco(String ico) { this.ico = ico; }

    public String getObchodniJmeno() { return obchodniJmeno; }
    public void setObchodniJmeno(String obchodniJmeno) { this.obchodniJmeno = obchodniJmeno; }

    public Sidlo getSidlo() { return sidlo; }
    public void setSidlo(Sidlo sidlo) { this.sidlo = sidlo; }

    public String getPravniForma() { return pravniForma; }
    public void setPravniForma(String pravniForma) { this.pravniForma = pravniForma; }

    public String getFinancniUrad() { return financniUrad; }
    public void setFinancniUrad(String financniUrad) { this.financniUrad = financniUrad; }

    public String getDatumVzniku() { return datumVzniku; }
    public void setDatumVzniku(String datumVzniku) { this.datumVzniku = datumVzniku; }

    public String getDatumAktualizace() { return datumAktualizace; }
    public void setDatumAktualizace(String datumAktualizace) { this.datumAktualizace = datumAktualizace; }

    public String getIcoId() { return icoId; }
    public void setIcoId(String icoId) { this.icoId = icoId; }

    public AdresaDorucovaci getAdresaDorucovaci() { return adresaDorucovaci; }
    public void setAdresaDorucovaci(AdresaDorucovaci adresaDorucovaci) { this.adresaDorucovaci = adresaDorucovaci; }

    public SeznamRegistraci getSeznamRegistraci() { return seznamRegistraci; }
    public void setSeznamRegistraci(SeznamRegistraci seznamRegistraci) { this.seznamRegistraci = seznamRegistraci; }

    public String getPrimarniZdroj() { return primarniZdroj; }
    public void setPrimarniZdroj(String primarniZdroj) { this.primarniZdroj = primarniZdroj; }

    public List<String> getCzNace() { return czNace; }
    public void setCzNace(List<String> czNace) { this.czNace = czNace; }
}