package org.example.supplychainx.service.Production;

import org.example.supplychainx.DTO.Production.BOMDTO;
import org.example.supplychainx.Mappers.Production.BOMMapper;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Production.BOM;
import org.example.supplychainx.Model.Production.Product;
import org.example.supplychainx.Repository.Production.BOMRepository;
import org.example.supplychainx.Service.Production.BOMService;
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
class BOMServiceTest {

    @Mock
    private BOMRepository bomRepository;

    @Mock
    private BOMMapper bomMapper;

    @InjectMocks
    private BOMService bomService;

    private BOM bom;
    private BOMDTO bomDTO;
    private Product product;
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
    }

    // Tests for getAllBOMs
    @Test
    void testGetAllBOMs_Success() {
        List<BOM> boms = Arrays.asList(bom);
        when(bomRepository.findAll()).thenReturn(boms);
        when(bomMapper.toDto(bom)).thenReturn(bomDTO);

        List<BOMDTO> result = bomService.getAllBOMs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdBOM());
        verify(bomRepository, times(1)).findAll();
        verify(bomMapper, times(1)).toDto(bom);
    }

    @Test
    void testGetAllBOMs_EmptyList() {
        when(bomRepository.findAll()).thenReturn(Collections.emptyList());

        List<BOMDTO> result = bomService.getAllBOMs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bomRepository, times(1)).findAll();
        verify(bomMapper, never()).toDto(any());
    }

    @Test
    void testGetAllBOMs_MultipleBOMs_LambdaMapping() {
        BOM bom2 = new BOM();
        bom2.setIdBOM(2L);
        bom2.setProduct(product);
        bom2.setMaterial(rawMaterial);
        bom2.setQuantity(10);

        BOMDTO bomDTO2 = new BOMDTO();
        bomDTO2.setIdBOM(2L);
        bomDTO2.setProduct_id(1L);
        bomDTO2.setMaterial_id(1L);
        bomDTO2.setQuantity(10);

        List<BOM> boms = Arrays.asList(bom, bom2);
        when(bomRepository.findAll()).thenReturn(boms);
        when(bomMapper.toDto(bom)).thenReturn(bomDTO);
        when(bomMapper.toDto(bom2)).thenReturn(bomDTO2);

        List<BOMDTO> result = bomService.getAllBOMs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getIdBOM());
        assertEquals(2L, result.get(1).getIdBOM());
        verify(bomRepository, times(1)).findAll();
        verify(bomMapper, times(2)).toDto(any(BOM.class));
    }

    // Tests for getBOMById
    @Test
    void testGetBOMById_Success() {
        when(bomRepository.findById(1L)).thenReturn(Optional.of(bom));
        when(bomMapper.toDto(bom)).thenReturn(bomDTO);

        BOMDTO result = bomService.getBOMById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdBOM());
        assertEquals(5, result.getQuantity());
        verify(bomRepository, times(1)).findById(1L);
        verify(bomMapper, times(1)).toDto(bom);
    }

    @Test
    void testGetBOMById_NotFound() {
        when(bomRepository.findById(999L)).thenReturn(Optional.empty());
        when(bomMapper.toDto(null)).thenReturn(null);

        BOMDTO result = bomService.getBOMById(999L);

        assertNull(result);
        verify(bomRepository, times(1)).findById(999L);
    }

    // Tests for getBOMsByProductId
    @Test
    void testGetBOMsByProductId_Success() {
        List<BOM> boms = Arrays.asList(bom);
        when(bomRepository.findAll()).thenReturn(boms);
        when(bomMapper.toDto(bom)).thenReturn(bomDTO);

        List<BOMDTO> result = bomService.getBOMsByProductId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getProduct_id());
        verify(bomRepository, times(1)).findAll();
    }

    @Test
    void testGetBOMsByProductId_MultipleBOMs_FilterLambda() {
        Product product2 = new Product();
        product2.setIdProduct(2L);
        product2.setName("Another Product");

        BOM bom2 = new BOM();
        bom2.setIdBOM(2L);
        bom2.setProduct(product2);
        bom2.setMaterial(rawMaterial);
        bom2.setQuantity(10);

        BOM bom3 = new BOM();
        bom3.setIdBOM(3L);
        bom3.setProduct(product);
        bom3.setMaterial(rawMaterial);
        bom3.setQuantity(15);

        BOMDTO bomDTO3 = new BOMDTO();
        bomDTO3.setIdBOM(3L);
        bomDTO3.setProduct_id(1L);
        bomDTO3.setMaterial_id(1L);
        bomDTO3.setQuantity(15);

        List<BOM> allBoms = Arrays.asList(bom, bom2, bom3);
        when(bomRepository.findAll()).thenReturn(allBoms);
        when(bomMapper.toDto(bom)).thenReturn(bomDTO);
        when(bomMapper.toDto(bom3)).thenReturn(bomDTO3);

        List<BOMDTO> result = bomService.getBOMsByProductId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(dto -> dto.getProduct_id().equals(1L)));
        verify(bomRepository, times(1)).findAll();
    }

    @Test
    void testGetBOMsByProductId_NullProduct_FilterLambda() {
        BOM bomWithoutProduct = new BOM();
        bomWithoutProduct.setIdBOM(2L);
        bomWithoutProduct.setProduct(null);
        bomWithoutProduct.setMaterial(rawMaterial);
        bomWithoutProduct.setQuantity(10);

        List<BOM> allBoms = Arrays.asList(bom, bomWithoutProduct);
        when(bomRepository.findAll()).thenReturn(allBoms);
        when(bomMapper.toDto(bom)).thenReturn(bomDTO);

        List<BOMDTO> result = bomService.getBOMsByProductId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdBOM());
        verify(bomRepository, times(1)).findAll();
    }

    @Test
    void testGetBOMsByProductId_NotFound() {
        List<BOM> boms = Arrays.asList(bom);
        when(bomRepository.findAll()).thenReturn(boms);

        List<BOMDTO> result = bomService.getBOMsByProductId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bomRepository, times(1)).findAll();
    }

    // Tests for createBOM
    @Test
    void testCreateBOM_Success() {
        when(bomMapper.toEntity(bomDTO)).thenReturn(bom);
        when(bomRepository.save(bom)).thenReturn(bom);
        when(bomMapper.toDto(bom)).thenReturn(bomDTO);

        BOMDTO result = bomService.createBOM(bomDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdBOM());
        assertEquals(5, result.getQuantity());
        verify(bomMapper, times(1)).toEntity(bomDTO);
        verify(bomRepository, times(1)).save(bom);
        verify(bomMapper, times(1)).toDto(bom);
    }

    @Test
    void testCreateBOM_WithNullValues() {
        BOMDTO emptyDTO = new BOMDTO();
        BOM emptyBOM = new BOM();

        when(bomMapper.toEntity(emptyDTO)).thenReturn(emptyBOM);
        when(bomRepository.save(emptyBOM)).thenReturn(emptyBOM);
        when(bomMapper.toDto(emptyBOM)).thenReturn(emptyDTO);

        BOMDTO result = bomService.createBOM(emptyDTO);

        assertNotNull(result);
        verify(bomRepository, times(1)).save(emptyBOM);
    }

    // Tests for save (entity)
    @Test
    void testSave_Success() {
        when(bomRepository.save(bom)).thenReturn(bom);

        BOM result = bomService.save(bom);

        assertNotNull(result);
        assertEquals(1L, result.getIdBOM());
        verify(bomRepository, times(1)).save(bom);
    }

    // Tests for updateBOM
    @Test
    void testUpdateBOM_Success() {
        BOMDTO updatedDTO = new BOMDTO();
        updatedDTO.setIdBOM(1L);
        updatedDTO.setProduct_id(1L);
        updatedDTO.setMaterial_id(1L);
        updatedDTO.setQuantity(20);

        BOM updatedBOM = new BOM();
        updatedBOM.setIdBOM(1L);
        updatedBOM.setProduct(product);
        updatedBOM.setMaterial(rawMaterial);
        updatedBOM.setQuantity(20);

        when(bomRepository.findById(1L)).thenReturn(Optional.of(bom));
        when(bomMapper.toEntity(updatedDTO)).thenReturn(updatedBOM);
        when(bomRepository.save(any(BOM.class))).thenReturn(updatedBOM);
        when(bomMapper.toDto(updatedBOM)).thenReturn(updatedDTO);

        BOMDTO result = bomService.updateBOM(1L, updatedDTO);

        assertNotNull(result);
        assertEquals(1L, result.getIdBOM());
        assertEquals(20, result.getQuantity());
        verify(bomRepository, times(1)).findById(1L);
        verify(bomRepository, times(1)).save(any(BOM.class));
    }

    @Test
    void testUpdateBOM_NotFound() {
        when(bomRepository.findById(999L)).thenReturn(Optional.empty());

        BOMDTO result = bomService.updateBOM(999L, bomDTO);

        assertNull(result);
        verify(bomRepository, times(1)).findById(999L);
        verify(bomRepository, never()).save(any());
    }

    @Test
    void testUpdateBOM_IdPreserved() {
        BOM bomToUpdate = new BOM();
        bomToUpdate.setQuantity(25);

        when(bomRepository.findById(1L)).thenReturn(Optional.of(bom));
        when(bomMapper.toEntity(bomDTO)).thenReturn(bomToUpdate);
        when(bomRepository.save(any(BOM.class))).thenAnswer(invocation -> {
            BOM savedBOM = invocation.getArgument(0);
            assertEquals(1L, savedBOM.getIdBOM());
            return savedBOM;
        });
        when(bomMapper.toDto(any(BOM.class))).thenReturn(bomDTO);

        BOMDTO result = bomService.updateBOM(1L, bomDTO);

        assertNotNull(result);
        verify(bomRepository, times(1)).save(any(BOM.class));
    }

    // Tests for deleteBOM
    @Test
    void testDeleteBOM_Success() {
        doNothing().when(bomRepository).deleteById(1L);

        bomService.deleteBOM(1L);

        verify(bomRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBOM_NonExistentId() {
        doNothing().when(bomRepository).deleteById(999L);

        bomService.deleteBOM(999L);

        verify(bomRepository, times(1)).deleteById(999L);
    }

    // Tests for deleteByProductId
    @Test
    void testDeleteByProductId_Success() {
        List<BOM> boms = Arrays.asList(bom);
        when(bomRepository.findAll()).thenReturn(boms);
        doNothing().when(bomRepository).deleteAll(boms);

        bomService.deleteByProductId(1L);

        verify(bomRepository, times(1)).findAll();
        verify(bomRepository, times(1)).deleteAll(boms);
    }

    @Test
    void testDeleteByProductId_MultipleBOMs_FilterLambda() {
        Product product2 = new Product();
        product2.setIdProduct(2L);

        BOM bom2 = new BOM();
        bom2.setIdBOM(2L);
        bom2.setProduct(product2);
        bom2.setMaterial(rawMaterial);
        bom2.setQuantity(10);

        BOM bom3 = new BOM();
        bom3.setIdBOM(3L);
        bom3.setProduct(product);
        bom3.setMaterial(rawMaterial);
        bom3.setQuantity(15);

        List<BOM> allBoms = Arrays.asList(bom, bom2, bom3);
        when(bomRepository.findAll()).thenReturn(allBoms);
        doNothing().when(bomRepository).deleteAll(anyList());

        bomService.deleteByProductId(1L);

        verify(bomRepository, times(1)).findAll();
        verify(bomRepository, times(1)).deleteAll(argThat(iter -> {
            List<BOM> list = (List<BOM>) iter;
            return list.size() == 2 &&
                list.stream().allMatch(b -> b.getProduct().getIdProduct().equals(1L));
        }));
    }

    @Test
    void testDeleteByProductId_NoMatchingBOMs() {
        List<BOM> boms = Arrays.asList(bom);
        when(bomRepository.findAll()).thenReturn(boms);
        doNothing().when(bomRepository).deleteAll(anyList());

        bomService.deleteByProductId(999L);

        verify(bomRepository, times(1)).findAll();
        verify(bomRepository, times(1)).deleteAll(argThat(iter -> {
            List<BOM> list = (List<BOM>) iter;
            return list.isEmpty();
        }));
    }

    @Test
    void testDeleteByProductId_WithNullProduct_FilterLambda() {
        BOM bomWithNullProduct = new BOM();
        bomWithNullProduct.setIdBOM(2L);
        bomWithNullProduct.setProduct(null);
        bomWithNullProduct.setMaterial(rawMaterial);
        bomWithNullProduct.setQuantity(10);

        List<BOM> allBoms = Arrays.asList(bom, bomWithNullProduct);
        when(bomRepository.findAll()).thenReturn(allBoms);
        doNothing().when(bomRepository).deleteAll(anyList());

        bomService.deleteByProductId(1L);

        verify(bomRepository, times(1)).findAll();
        verify(bomRepository, times(1)).deleteAll(argThat(iter -> {
            List<BOM> list = (List<BOM>) iter;
            return list.size() == 1 &&
                list.get(0).getIdBOM().equals(1L);
        }));
    }

    // Edge case tests
    @Test
    void testGetAllBOMs_StreamMapping_PreservesOrder() {
        BOM bom1 = new BOM();
        bom1.setIdBOM(1L);
        bom1.setQuantity(5);

        BOM bom2 = new BOM();
        bom2.setIdBOM(2L);
        bom2.setQuantity(10);

        BOM bom3 = new BOM();
        bom3.setIdBOM(3L);
        bom3.setQuantity(15);

        BOMDTO dto1 = new BOMDTO();
        dto1.setIdBOM(1L);

        BOMDTO dto2 = new BOMDTO();
        dto2.setIdBOM(2L);

        BOMDTO dto3 = new BOMDTO();
        dto3.setIdBOM(3L);

        List<BOM> boms = Arrays.asList(bom1, bom2, bom3);
        when(bomRepository.findAll()).thenReturn(boms);
        when(bomMapper.toDto(bom1)).thenReturn(dto1);
        when(bomMapper.toDto(bom2)).thenReturn(dto2);
        when(bomMapper.toDto(bom3)).thenReturn(dto3);

        List<BOMDTO> result = bomService.getAllBOMs();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0).getIdBOM());
        assertEquals(2L, result.get(1).getIdBOM());
        assertEquals(3L, result.get(2).getIdBOM());
    }
}
