package com.readbetter.main.components;

import com.readbetter.main.exceptions.RegistrationException;
import com.readbetter.main.model.AppUser;
import com.readbetter.main.model.Privilege;
import com.readbetter.main.model.Role;
import com.readbetter.main.repository.AppUserRepository;
import com.readbetter.main.repository.PrivilegeRepository;
import com.readbetter.main.repository.RoleRepository;
import com.readbetter.main.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;


//
//
//import com.readbetter.main.model.AppUser;
//
//import com.readbetter.main.model.Definition;
//import com.readbetter.main.model.Entry;
//import com.readbetter.main.repository.AppUserRepository;
//import com.readbetter.main.repository.DefinitionRepository;
//import com.readbetter.main.repository.EntryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class DataInitializer  {
//
//    private EntryRepository entryRepository;
//    private DefinitionRepository definitionRepository;
//    private AppUserRepository appUserRepository;
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    public DataInitializer(EntryRepository entryRepository, DefinitionRepository definitionRepository, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
//        this.entryRepository = entryRepository;
//        this.definitionRepository = definitionRepository;
//        this.appUserRepository = appUserRepository;
//        this.passwordEncoder = passwordEncoder;
//
//        loadUser();
//        loadData();
//    }
//
//    private void loadData () {
//        List<Definition> definitions = new ArrayList<Definition>();
//        definitions.add(new Definition("def1"));
//        definitions.add(new Definition("def2"));
//        definitions.add(new Definition("def3"));
//        Entry entry = new Entry("entry", definitions);
//        entryRepository.saveAndFlush(entry);
//    }
//    private void loadUser () {
//
//        AppUser user = new AppUser();
//        user.setUsername("admin");
//        user.setPassword(this.passwordEncoder.encode("admin"));
//        appUserRepository.saveAndFlush(user);
//    }
//}
@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;
    @Autowired
   private AppUserService appUserService;
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ADMIN", adminPrivileges);
        createRoleIfNotFound("USER", Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName("ADMIN");
        AppUser user = new AppUser();
        user.setUsername("Admin");
        user.setPassword("wkretaczek");
        user.setEmail("contact@read-better.pl");
        user.setRoles(Arrays.asList(adminRole));
        user.setEnabled(true);
        try {
            appUserService.register(user);
        } catch (RegistrationException e) {
            e.printStackTrace();
        }

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, List<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}