package com.qubiz.server.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.qubiz.server.dao.ClientDao;
import com.qubiz.server.dao.ExpertDao;
import com.qubiz.server.entity.Client;
import com.qubiz.server.entity.Expert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 16.06.2018 #
 ******************************
*/
@Service
public class UserRegister {

    private final ClientDao clientDao;
    private final ExpertDao expertDao;

    @Autowired
    public UserRegister(ClientDao clientDao, ExpertDao expertDao) {
        this.clientDao = clientDao;
        this.expertDao = expertDao;
    }


    public void registerClient(GoogleIdToken.Payload payload) {
        Optional<Client> client = clientDao.findClientByUserTokenId(payload.getSubject());
        Client newClient = client.orElseGet(Client::new); // If is present, get and update details, or else create new

        String name = (String) payload.get("name");
        String email = payload.getEmail();
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");
        String userTokenId = payload.getSubject();

        //TODO make a validator

        newClient.setUsername(name);
        newClient.setFirstName(givenName);
        newClient.setLastName(familyName);
        newClient.setUserTokenId(userTokenId);

        clientDao.save(newClient);
    }

    public void registerExpert(GoogleIdToken.Payload payload) {
        Optional<Expert> client = expertDao.findClientByUserTokenId(payload.getSubject());
        Expert newExpert = client.orElseGet(Expert::new); // If is present, get and update details, or else create new

        String name = (String) payload.get("name");
        String email = payload.getEmail();
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");
        String userTokenId = payload.getSubject();

        //TODO make a validator

        newExpert.setUsername(name);
        newExpert.setFirstName(givenName);
        newExpert.setLastName(familyName);
        newExpert.setUserTokenId(userTokenId);

        expertDao.save(newExpert);
    }
}
