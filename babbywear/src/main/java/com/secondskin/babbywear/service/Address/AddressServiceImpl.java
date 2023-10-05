package com.secondskin.babbywear.service.Address;

import com.secondskin.babbywear.model.Address;

import com.secondskin.babbywear.repository.AddressRepository;
import com.secondskin.babbywear.service.variant.ProductNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AddressServiceImpl implements AddressService{


    @Autowired
    AddressRepository addressRepository;


    @Override
    public Address getAddressById(Long addressId) {
        Optional<Address> addressOptional = addressRepository.findById(addressId);

        if(addressOptional.isPresent()){
            return addressOptional.get();
        }
        else {
            throw  new ProductNotFoundException("Not found");
        }

    }

    @Override
    public List<Address> getByUserName(String userName) {


        return null;
    }

    @Override
    public void updateAddress(Address address ,Long id) {

        Optional<Address> optionalAddress = addressRepository.findById(id);

        if(optionalAddress.isPresent()){

            Address newAddress = optionalAddress.get();
            newAddress.setFlat(address.getFlat());
            newAddress.setArea(address.getArea());
            newAddress.setTown(address.getTown());
            newAddress.setCity(address.getCity());
            newAddress.setState(address.getState());
            newAddress.setPin(address.getPin());
            newAddress.setLandMark(address.getLandMark());
            addressRepository.save(newAddress);
        }
        else {
            throw new RuntimeException("Address not found");
        }


    }

    @Override
    public void deleteById(Long id) {

        Optional<Address> address = addressRepository.findById(id);
        address.ifPresent(useraddress->{
            useraddress.setDeleted(true);
            addressRepository.save(useraddress);
        });
    }
}
