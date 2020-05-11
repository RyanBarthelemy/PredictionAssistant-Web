package com.axlor.predictionassistantrest.model.mini;

public class ContractMini {
    private String name;
    private int id;
    private String href;

    public ContractMini(String name, int id, String href) {
        this.name = name;
        this.id = id;
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
