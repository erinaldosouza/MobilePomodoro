package br.edu.fa7.mobilepomodoro.business;

import java.io.Serializable;
import java.util.List;

import br.edu.fa7.mobilepomodoro.model.PomoTask;
import br.edu.fa7.mobilepomodoro.repository.PomoTaskDao;

/**
 * Created by erinaldo.souza on 05/06/2016.
 */
public class PomoTaskBusiness extends GenericBusiness<PomoTask>  implements Serializable {

    public PomoTaskBusiness(PomoTaskDao dao) {
        super(dao);
    }

}
