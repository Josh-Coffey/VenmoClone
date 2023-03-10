package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.dao.WalletDao;
import com.techelevator.tenmo.model.Authority;
import com.techelevator.tenmo.model.Wallet;
import com.techelevator.tenmo.model.WalletDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wallets")
@PreAuthorize("isAuthenticated()")
public class WalletController {

    /*
    ########################################   Attributes   ##########################################
     */

    private WalletDao walletDao;
    private UserDao userDao;

    /*
   ########################################   Constructor   ##########################################
    */

    public WalletController(WalletDao walletDao, UserDao userDao) {
        this.walletDao = walletDao;
        this.userDao=userDao;
    }

    /*
   ########################################  API Endpoints  ##########################################
    */

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") //TODO Normal user should be able to view their own balance
    public List<WalletDto> list(@RequestParam(required = false, name = "user-id") Integer userId){

        List<Wallet> wallets = walletDao.listWallets();
        List<WalletDto> walletDtos = new ArrayList<>();

        if(userId != null){
            Wallet wallet = walletDao.getWalletByUser(userId);
            walletDtos.add(mapWalletToDto(wallet));
        } else {
            for (Wallet wallet: wallets){
                walletDtos.add(mapWalletToDto(wallet));
            }

        }
        return walletDtos;
    }

    @GetMapping (path="/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public WalletDto get(@PathVariable int id, Principal principal){

        Wallet wallet = walletDao.getWallet(id);
        int currentUserId=userDao.findIdByUsername(principal.getName());

        if(wallet.getUserId()!=currentUserId && !userDao.getUserById(currentUserId).getAuthorities().contains(new Authority("ROLE_ADMIN"))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only access your wallet.");
        }

        return mapWalletToDto(wallet);
    }

    /*
    ########################################  Helper Methods  ##########################################
     */

    //Helper Function to map a Wallet object to a WalletDto
    private WalletDto mapWalletToDto(Wallet wallet) {

        WalletDto walletDto = new WalletDto();
        walletDto.setBalance(wallet.getBalance());
        walletDto.setId(wallet.getId());
        walletDto.setUserId(wallet.getUserId());

        return walletDto;
    }


}