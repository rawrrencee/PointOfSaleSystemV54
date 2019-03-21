package ejb.session.stateless;

import entity.ProductEntity;
import java.util.List;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewProductException;
import util.exception.DeleteProductException;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UpdateProductException;



public interface ProductEntityControllerRemote
{
    ProductEntity createNewProduct(ProductEntity newProductEntity, Long categoryId, List<Long> tagIds) throws InputDataValidationException, CreateNewProductException;
  
    List<ProductEntity> retrieveAllProducts();
    
    List<ProductEntity> searchProductsByName(String searchString);
    
    List<ProductEntity> filterProductsByCategory(Long categoryId) throws CategoryNotFoundException;
    
    List<ProductEntity> filterProductsByTags(List<Long> tagIds, String condition);

    ProductEntity retrieveProductByProductId(Long productId) throws ProductNotFoundException;

    ProductEntity retrieveProductByProductSkuCode(String skuCode) throws ProductNotFoundException;

    void updateProduct(ProductEntity productEntity, Long categoryId, List<Long> tagIds) throws InputDataValidationException, ProductNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdateProductException;
    
    void deleteProduct(Long productId) throws ProductNotFoundException, DeleteProductException;
}
