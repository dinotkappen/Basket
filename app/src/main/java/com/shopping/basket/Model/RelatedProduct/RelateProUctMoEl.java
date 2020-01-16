
package com.shopping.basket.Model.RelatedProduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RelateProUctMoEl {

    @SerializedName("status")
    @Expose
    private String tatu;
    @SerializedName("related_products")
    @Expose
    private List<Relate_proUct> relate_proUct = null;

    public String getTatu() {
        return tatu;
    }

    public void setTatu(String tatu) {
        this.tatu = tatu;
    }

    public List<Relate_proUct> getRelate_proUct() {
        return relate_proUct;
    }

    public void setRelate_proUct(List<Relate_proUct> relate_proUct) {
        this.relate_proUct = relate_proUct;
    }

}
