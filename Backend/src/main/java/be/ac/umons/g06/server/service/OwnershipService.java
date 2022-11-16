package be.ac.umons.g06.server.service;

import be.ac.umons.g06.server.model.ownership.Ownership;
import be.ac.umons.g06.server.repository.OwnershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * OwnershipService
 */
@Service
public class OwnershipService {
    /**
     * Object of OwnershipRepository
     */
    @Autowired
    private OwnershipRepository repository;

    /**
     *
     * @param ownership the ownership we want to save
     * @return the saved ownership
     */
    public Ownership save(Ownership ownership) {
        return repository.save(ownership);
    }

    /**
     * Get all ownerships
     * @return List of ownerships
     */
    public Iterable<Ownership> getOwnerships() {
        return repository.findAll();
    }
}
