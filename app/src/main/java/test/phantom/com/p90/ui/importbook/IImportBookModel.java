package test.phantom.com.p90.ui.importbook;


import java.io.File;

import io.reactivex.Observable;
import test.phantom.com.p90.entity.LocBookShelfBean;

public interface IImportBookModel {

    Observable<LocBookShelfBean> importBook(File book);
}
