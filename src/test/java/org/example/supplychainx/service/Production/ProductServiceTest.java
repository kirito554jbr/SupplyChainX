package org.example.supplychainx.service.Production;

import org.example.supplychainx.DTO.Production.BOMDTO;
import org.example.supplychainx.DTO.Production.ProductDTO;
import org.example.supplychainx.DTO.Production.ProductResponseDTO;
import org.example.supplychainx.Mappers.Approvisionnement.RawMaterialMapper;
import org.example.supplychainx.Mappers.Production.BOMMapper;
import org.example.supplychainx.Mappers.Production.ProductMapper;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Production.BOM;
import org.example.supplychainx.Model.Production.Product;
import org.example.supplychainx.Repository.Production.ProductRepository;
import org.example.supplychainx.Service.Approvisionnement.RawMateialService;
import org.example.supplychainx.Service.Production.BOMService;
import org.example.supplychainx.Service.Production.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private BOMService bomService;

    @Mock
    private BOMMapper bomMapper;

    @Mock
    private RawMateialService rawMaterialService;

    @Mock
    private RawMaterialMapper rawMaterialMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;
    private ProductResponseDTO productResponseDTO;
    private BOM bom;
    private BOMDTO bomDTO;
    private RawMaterial rawMaterial;

    @BeforeEach
    void setUp() {
        // Setup Product
        product = new Product();
        product.setIdProduct(1L);
        product.setName("Test Product");
        product.setProductionTime(10);
        product.setCost(100.0);
        product.setStock(50);

        // Setup RawMaterial
        rawMaterial = new RawMaterial();
        rawMaterial.setIdMaterial(1L);
        rawMaterial.setName("Steel");

        // Setup BOM
        bom = new BOM();
        bom.setIdBOM(1L);
        bom.setProduct(product);
        bom.setMaterial(rawMaterial);
        bom.setQuantity(5);

        // Setup BOMDTO
        bomDTO = new BOMDTO();
        bomDTO.setIdBOM(1L);
        bomDTO.setProduct_id(1L);
        bomDTO.setMaterial_id(1L);
        bomDTO.setQuantity(5);

        // Setup ProductDTO
        productDTO = new ProductDTO();
        productDTO.setIdProduct(1L);
        productDTO.setName("Test Product");
        productDTO.setProductionTime(10);
        productDTO.setCost(100.0);
        productDTO.setStock(50);
        productDTO.setBoms(Arrays.asList(bomDTO));

        // Setup ProductResponseDTO
        productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setIdProduct(1L);
        productResponseDTO.setName("Test Product");
        productResponseDTO.setProductionTime(10);
        productResponseDTO.setCost(100.0);
        productResponseDTO.setStock(50);

        product.setBoms(Arrays.asList(bom));
    }

    // Tests for getById
    @Test
    void testGetById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDTO);

        ProductDTO result = productService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdProduct());
        assertEquals("Test Product", result.getName());
        assertEquals(100.0, result.getCost());
        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void testGetById_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        when(productMapper.toDto(null)).thenReturn(null);

        ProductDTO result = productService.getById(999L);

        assertNull(result);
        verify(productRepository, times(1)).findById(999L);
    }

    // Tests for getAll
    @Test
    void testGetAll_Success() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(product)).thenReturn(productDTO);

        List<ProductDTO> result = productService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void testGetAll_EmptyList() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductDTO> result = productService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, never()).toDto(any());
    }

    @Test
    void testGetAll_MultipleProducts_LambdaMapping() {
        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setName("Second Product");
        product2.setProductionTime(15);
        product2.setCost(150.0);
        product2.setStock(30);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setIdProduct(2L);
        productDTO2.setName("Second Product");

        List<Product> products = Arrays.asList(product, product2);
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(product)).thenReturn(productDTO);
        when(productMapper.toDto(product2)).thenReturn(productDTO2);

        List<ProductDTO> result = productService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Product", result.get(0).getName());
        assertEquals("Second Product", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(2)).toDto(any(Product.class));
    }

    // Tests for save
    @Test
    void testSave_Success() {
        when(productMapper.toEntity(productDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(bomMapper.toEntity(bomDTO)).thenReturn(bom);
        when(rawMaterialService.findEntityById(1L)).thenReturn(rawMaterial);
        when(bomService.save(any(BOM.class))).thenReturn(bom);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);

        ProductResponseDTO result = productService.save(productDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdProduct());
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).save(product);
        verify(bomService, times(1)).save(any(BOM.class));
        verify(productMapper, times(1)).toResponseDTO(product);
    }

    @Test
    void testSave_WithMultipleBOMs_LambdaMapping() {
        BOMDTO bomDTO2 = new BOMDTO();
        bomDTO2.setIdBOM(2L);
        bomDTO2.setProduct_id(1L);
        bomDTO2.setMaterial_id(2L);
        bomDTO2.setQuantity(10);

        RawMaterial rawMaterial2 = new RawMaterial();
        rawMaterial2.setIdMaterial(2L);
        rawMaterial2.setName("Aluminum");

        BOM bom2 = new BOM();
        bom2.setIdBOM(2L);
        bom2.setProduct(product);
        bom2.setMaterial(rawMaterial2);
        bom2.setQuantity(10);

        productDTO.setBoms(Arrays.asList(bomDTO, bomDTO2));

        when(productMapper.toEntity(productDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(bomMapper.toEntity(bomDTO)).thenReturn(bom);
        when(bomMapper.toEntity(bomDTO2)).thenReturn(bom2);
        when(rawMaterialService.findEntityById(1L)).thenReturn(rawMaterial);
        when(rawMaterialService.findEntityById(2L)).thenReturn(rawMaterial2);
        when(bomService.save(any(BOM.class))).thenReturn(bom, bom2);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);

        ProductResponseDTO result = productService.save(productDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
        verify(bomMapper, times(2)).toEntity(any(BOMDTO.class));
        verify(rawMaterialService, times(1)).findEntityById(1L);
        verify(rawMaterialService, times(1)).findEntityById(2L);
        verify(bomService, times(2)).save(any(BOM.class));
    }

    @Test
    void testSave_EmptyBOMs() {
        productDTO.setBoms(Collections.emptyList());

        when(productMapper.toEntity(productDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);

        ProductResponseDTO result = productService.save(productDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
        verify(bomService, never()).save(any(BOM.class));
    }

    // Tests for deleteById
    @Test
    void testDeleteById_Success() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteById(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NonExistentId() {
        doNothing().when(productRepository).deleteById(999L);

        productService.deleteById(999L);

        verify(productRepository, times(1)).deleteById(999L);
    }

    // Tests for updateById
    @Test
    void testUpdateById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(bomMapper.toEntity(bomDTO)).thenReturn(bom);
        when(rawMaterialService.findEntityById(1L)).thenReturn(rawMaterial);
        when(bomService.save(any(BOM.class))).thenReturn(bom);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);
        doNothing().when(bomService).deleteByProductId(1L);

        ProductResponseDTO result = productService.updateById(1L, productDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdProduct());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(bomService, times(1)).deleteByProductId(1L);
        verify(bomService, times(1)).save(any(BOM.class));
    }

    @Test
    void testUpdateById_NotFound_ThrowsException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.updateById(999L, productDTO));

        assertEquals("Product not found with id: 999", exception.getMessage());
        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).save(any());
        verify(bomService, never()).deleteByProductId(any());
    }

    @Test
    void testUpdateById_WithNullBOMs() {
        productDTO.setBoms(null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);
        doNothing().when(bomService).deleteByProductId(1L);

        ProductResponseDTO result = productService.updateById(1L, productDTO);

        assertNotNull(result);
        verify(bomService, times(1)).deleteByProductId(1L);
        verify(bomService, never()).save(any(BOM.class));
    }

    @Test
    void testUpdateById_WithEmptyBOMs() {
        productDTO.setBoms(Collections.emptyList());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);
        doNothing().when(bomService).deleteByProductId(1L);

        ProductResponseDTO result = productService.updateById(1L, productDTO);

        assertNotNull(result);
        verify(bomService, times(1)).deleteByProductId(1L);
        verify(bomService, never()).save(any(BOM.class));
    }

    @Test
    void testUpdateById_WithMultipleBOMs_LambdaMapping() {
        BOMDTO bomDTO2 = new BOMDTO();
        bomDTO2.setIdBOM(2L);
        bomDTO2.setMaterial_id(2L);
        bomDTO2.setQuantity(10);

        RawMaterial rawMaterial2 = new RawMaterial();
        rawMaterial2.setIdMaterial(2L);

        BOM bom2 = new BOM();
        bom2.setIdBOM(2L);

        productDTO.setBoms(Arrays.asList(bomDTO, bomDTO2));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(bomMapper.toEntity(bomDTO)).thenReturn(bom);
        when(bomMapper.toEntity(bomDTO2)).thenReturn(bom2);
        when(rawMaterialService.findEntityById(1L)).thenReturn(rawMaterial);
        when(rawMaterialService.findEntityById(2L)).thenReturn(rawMaterial2);
        when(bomService.save(any(BOM.class))).thenReturn(bom, bom2);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);
        doNothing().when(bomService).deleteByProductId(1L);

        ProductResponseDTO result = productService.updateById(1L, productDTO);

        assertNotNull(result);
        verify(bomService, times(1)).deleteByProductId(1L);
        verify(bomService, times(2)).save(any(BOM.class));
        verify(rawMaterialService, times(1)).findEntityById(1L);
        verify(rawMaterialService, times(1)).findEntityById(2L);
    }

    @Test
    void testUpdateById_ProductFieldsUpdated() {
        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setName("Updated Product");
        updatedDTO.setProductionTime(20);
        updatedDTO.setCost(200.0);
        updatedDTO.setStock(100);
        updatedDTO.setBoms(Collections.emptyList());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product saved = invocation.getArgument(0);
            assertEquals("Updated Product", saved.getName());
            assertEquals(20, saved.getProductionTime());
            assertEquals(200.0, saved.getCost());
            assertEquals(100, saved.getStock());
            return saved;
        });
        when(productMapper.toResponseDTO(any(Product.class))).thenReturn(productResponseDTO);
        doNothing().when(bomService).deleteByProductId(1L);

        ProductResponseDTO result = productService.updateById(1L, updatedDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // Tests for getByName
    @Test
    void testGetByName_Success() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findByNameContainingIgnoreCase("Test")).thenReturn(products);
        when(productMapper.toDto(product)).thenReturn(productDTO);

        List<ProductDTO> result = productService.getByName("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Test");
    }

    @Test
    void testGetByName_NoResults() {
        when(productRepository.findByNameContainingIgnoreCase("NonExistent")).thenReturn(Collections.emptyList());

        List<ProductDTO> result = productService.getByName("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("NonExistent");
    }

    @Test
    void testGetByName_MultipleResults_LambdaMapping() {
        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setName("Test Product 2");

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setIdProduct(2L);
        productDTO2.setName("Test Product 2");

        List<Product> products = Arrays.asList(product, product2);
        when(productRepository.findByNameContainingIgnoreCase("Test")).thenReturn(products);
        when(productMapper.toDto(product)).thenReturn(productDTO);
        when(productMapper.toDto(product2)).thenReturn(productDTO2);

        List<ProductDTO> result = productService.getByName("Test");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Test");
        verify(productMapper, times(2)).toDto(any(Product.class));
    }

    @Test
    void testGetByName_CaseInsensitive() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findByNameContainingIgnoreCase("test")).thenReturn(products);
        when(productMapper.toDto(product)).thenReturn(productDTO);

        List<ProductDTO> result = productService.getByName("test");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("test");
    }

    // Tests for findByNameContaining
    @Test
    void testFindByNameContaining_Success() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findByNameContainingIgnoreCase("Test")).thenReturn(products);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);

        List<ProductResponseDTO> result = productService.findByNameContaining("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Test");
        verify(productMapper, times(1)).toResponseDTO(product);
    }

    @Test
    void testFindByNameContaining_NoResults() {
        when(productRepository.findByNameContainingIgnoreCase("NonExistent")).thenReturn(Collections.emptyList());

        List<ProductResponseDTO> result = productService.findByNameContaining("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("NonExistent");
    }

    @Test
    void testFindByNameContaining_MultipleResults_LambdaMapping() {
        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setName("Test Product 2");

        ProductResponseDTO productResponseDTO2 = new ProductResponseDTO();
        productResponseDTO2.setIdProduct(2L);
        productResponseDTO2.setName("Test Product 2");

        List<Product> products = Arrays.asList(product, product2);
        when(productRepository.findByNameContainingIgnoreCase("Test")).thenReturn(products);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);
        when(productMapper.toResponseDTO(product2)).thenReturn(productResponseDTO2);

        List<ProductResponseDTO> result = productService.findByNameContaining("Test");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Product", result.get(0).getName());
        assertEquals("Test Product 2", result.get(1).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Test");
        verify(productMapper, times(2)).toResponseDTO(any(Product.class));
    }

    // Edge case tests
    @Test
    void testSave_BOMMapping_PreservesOrder() {
        BOMDTO bomDTO1 = new BOMDTO();
        bomDTO1.setIdBOM(1L);
        bomDTO1.setMaterial_id(1L);
        bomDTO1.setQuantity(5);

        BOMDTO bomDTO2 = new BOMDTO();
        bomDTO2.setIdBOM(2L);
        bomDTO2.setMaterial_id(2L);
        bomDTO2.setQuantity(10);

        BOMDTO bomDTO3 = new BOMDTO();
        bomDTO3.setIdBOM(3L);
        bomDTO3.setMaterial_id(3L);
        bomDTO3.setQuantity(15);

        productDTO.setBoms(Arrays.asList(bomDTO1, bomDTO2, bomDTO3));

        when(productMapper.toEntity(productDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(bomMapper.toEntity(any(BOMDTO.class))).thenReturn(bom);
        when(rawMaterialService.findEntityById(any())).thenReturn(rawMaterial);
        when(bomService.save(any(BOM.class))).thenReturn(bom);
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);

        ProductResponseDTO result = productService.save(productDTO);

        assertNotNull(result);
        verify(bomMapper, times(3)).toEntity(any(BOMDTO.class));
        verify(bomService, times(3)).save(any(BOM.class));
    }

    @Test
    void testUpdateById_BOMMapping_ChainedLambdas() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(bomMapper.toEntity(bomDTO)).thenReturn(bom);
        when(rawMaterialService.findEntityById(1L)).thenReturn(rawMaterial);
        when(bomService.save(any(BOM.class))).thenAnswer(invocation -> {
            BOM savedBOM = invocation.getArgument(0);
            assertNotNull(savedBOM.getProduct());
            assertNotNull(savedBOM.getMaterial());
            assertEquals(5, savedBOM.getQuantity());
            return savedBOM;
        });
        when(productMapper.toResponseDTO(product)).thenReturn(productResponseDTO);
        doNothing().when(bomService).deleteByProductId(1L);

        ProductResponseDTO result = productService.updateById(1L, productDTO);

        assertNotNull(result);
        verify(bomService, times(1)).save(any(BOM.class));
    }
}
