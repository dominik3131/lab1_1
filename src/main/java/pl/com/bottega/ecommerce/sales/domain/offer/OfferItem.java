/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package pl.com.bottega.ecommerce.sales.domain.offer;

import java.math.BigDecimal;

public class OfferItem {

    // product
    private ProductSnapshot product;

    private int quantity;

    private Money totalCost;

    // discount
    private Discount discount;

    public OfferItem(ProductSnapshot product, int quantity, Discount discount) {

        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
        BigDecimal discountValue = new BigDecimal(0);
        if (discount != null) {
            if (!product.getPrice()
                        .getCurrency()
                        .equals(discount.getValue()
                                        .getCurrency())) {
                throw new IllegalArgumentException("currencies doesn't match");
            }
            discountValue = discountValue.add(discount.getValue()
                                                      .getValue());
        }

        this.totalCost = new Money(product.getPrice()
                                          .getCurrency(),
                product.getPrice()
                       .getValue()
                       .multiply(new BigDecimal(quantity))
                       .subtract(discountValue));
    }

    public OfferItem(ProductSnapshot product, int quantity) {
        this(product, quantity, null);
    }

    /**
     * @return the product
     */
    public ProductSnapshot getProduct() {
        return product;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return the totalCost
     */
    public Money getTotalCost() {
        return totalCost;
    }

    /**
     * @return the discount
     */
    public Discount getDiscount() {
        return discount;
    }

    /**
     *
     * @param item
     * @param delta
     *            acceptable percentage difference
     * @return
     */
    public boolean sameAs(OfferItem other, double delta) {
        if (product.getPrice()
                   .getValue() == null) {
            if (other.product.getPrice()
                             .getValue() != null) {
                return false;
            }
        } else if (!product.getPrice()
                           .getValue()
                           .equals(other.product.getPrice()
                                                .getValue())) {
            return false;
        }
        if (product.getName() == null) {
            if (other.product.getName() != null) {
                return false;
            }
        } else if (!product.getName()
                           .equals(other.product.getName())) {
            return false;
        }

        if (product.getId() == null) {
            if (other.product.getId() != null) {
                return false;
            }
        } else if (!product.getId()
                           .equals(other.product.getId())) {
            return false;
        }
        if (product.getType() != other.product.getType()) {
            return false;
        }

        if (quantity != other.quantity) {
            return false;
        }

        BigDecimal max;
        BigDecimal min;
        if (totalCost.getValue()
                     .compareTo(other.totalCost.getValue()) > 0) {
            max = totalCost.getValue();
            min = other.totalCost.getValue();
        } else {
            max = other.totalCost.getValue();
            min = totalCost.getValue();
        }

        BigDecimal difference = max.subtract(min);
        BigDecimal acceptableDelta = max.multiply(BigDecimal.valueOf(delta / 100));

        return acceptableDelta.compareTo(difference) > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (discount == null ? 0 : discount.hashCode());
        result = prime * result + (product == null ? 0 : product.hashCode());
        result = prime * result + quantity;
        result = prime * result + (totalCost == null ? 0 : totalCost.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OfferItem other = (OfferItem) obj;
        if (discount == null) {
            if (other.discount != null) {
                return false;
            }
        } else if (!discount.equals(other.discount)) {
            return false;
        }
        if (product == null) {
            if (other.product != null) {
                return false;
            }
        } else if (!product.equals(other.product)) {
            return false;
        }
        if (quantity != other.quantity) {
            return false;
        }
        if (totalCost == null) {
            if (other.totalCost != null) {
                return false;
            }
        } else if (!totalCost.equals(other.totalCost)) {
            return false;
        }
        return true;
    }

}
