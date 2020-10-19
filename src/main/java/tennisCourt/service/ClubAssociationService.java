package tennisCourt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tennisCourt.model.ClubAssociation;
import tennisCourt.repo.ClubAssociationRepository;

import java.util.List;

@Service
@Transactional
public class ClubAssociationService {

    private ClubAssociationRepository repo;

    @Autowired
    public ClubAssociationService(ClubAssociationRepository clubAssociationRepository) {
        this.repo = clubAssociationRepository;
    }

    public List<ClubAssociation> listAll() {
        return repo.findAll();
    }

    public void save(ClubAssociation clubAssociation){
        repo.save(clubAssociation);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public ClubAssociation getByUserId(Long id) {
        return repo.findByUserId(id);
    }

    public ClubAssociation getById(Long id) {
        return repo.findByIdParam(id);
    }
}
