package app.test.com.myapplication.model;

/**
 * Created by anupama.shelke on 9/28/2016.
 */
public class Product {
    private String productName, VendorName,VendorAddress,thumbnailUrl,phoneNumber;
    private int productPrice,productId;

    public Product() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String VendorName) {
        this.VendorName = VendorName;
    }

    public String getVendorAddress() {
        return VendorAddress;
    }

    public void setVendorAddress(String VendorAddress) {
        this.VendorAddress = VendorAddress;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getproductPrice() {
        return productPrice;
    }

    public void setproductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //setId
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}


