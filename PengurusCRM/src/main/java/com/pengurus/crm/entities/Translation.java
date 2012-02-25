package com.pengurus.crm.entities;

import com.pengurus.crm.shared.dto.TranslationDTO;

public class Translation {

    private Long id;
    private TranslationType type;
    private Language lfrom;
    private Language lto;
    private Price defaultPrice;

    public Translation() {
        super();
    }

    public Translation(TranslationType type, Language lfrom, Language lto,
                       Price defaultPrice) {
        super();
        this.type = type;
        this.lfrom = lfrom;
        this.lto = lto;
        this.defaultPrice = defaultPrice;
    }

    public Price getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(Price defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public TranslationType getType() {
        return type;
    }

    public void setType(TranslationType type) {
        this.type = type;
    }

    public Language getLfrom() {
        return lfrom;
    }

    public void setLfrom(Language lfrom) {
        this.lfrom = lfrom;
    }

    public Language getLto() {
        return lto;
    }

    public void setLto(Language lto) {
        this.lto = lto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public TranslationDTO toDTO() {
		TranslationDTO tDTO = new TranslationDTO();
		if(defaultPrice != null)
			tDTO.setDefaultPrice(defaultPrice.toDTO());
		if(lfrom != null)
			tDTO.setFrom(lfrom.toDTO());
		if(lto != null)
			tDTO.setTo(lto.toDTO());
		if(type != null)
			tDTO.setType(type.toDTO());
		return tDTO;
	}
    
}
