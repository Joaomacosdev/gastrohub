package br.com.gastrohub.address.controller;

import br.com.gastrohub.address.dto.request.AddressRequestDTO;
import br.com.gastrohub.address.dto.request.AddressUpdateDTO;
import br.com.gastrohub.address.dto.response.AddressResponseDTO;
import br.com.gastrohub.address.service.AddressCommandService;
import br.com.gastrohub.address.service.AddressQueryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressCommandService addressCommandService;
    private final AddressQueryService addressQueryService;

    public AddressController(AddressCommandService addressCommandService, AddressQueryService addressQueryService) {
        this.addressCommandService = addressCommandService;
        this.addressQueryService = addressQueryService;
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress( @Valid @RequestBody AddressRequestDTO dto, UriComponentsBuilder uriBuilder){
        AddressResponseDTO addressResponse = addressCommandService.createAddress(dto);
        URI uri = uriBuilder.path("/{id}").buildAndExpand(addressResponse.id()).toUri();
        return ResponseEntity.created(uri).body(addressResponse);
    }

    @GetMapping("/{id}")
    private ResponseEntity<AddressResponseDTO> findById(@PathVariable UUID id){
        return ResponseEntity.ok().body(addressQueryService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> findAll(){
        return ResponseEntity.ok().body(addressQueryService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> updateAddress(@PathVariable UUID id, @Valid @RequestBody AddressUpdateDTO dto){
        return ResponseEntity.ok().body(addressCommandService.updateAddress(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID id){
        addressCommandService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }



}
