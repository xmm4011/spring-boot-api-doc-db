package top.webdevelop.gull.apidoc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by xumingming on 2018/3/24.
 */
public class APIDocMetadataGenerator {
    private APIDocService apiDocService;

    public APIDocMetadataGenerator() {
        this.apiDocService = new APIDocService();
    }

    public void generate(APIDocProject project) {
        APIDocProject oldProject = apiDocService.findAPIDocProjectDetail(project.getName(), true);
        if (oldProject != null) {
            mergeMenu(project.getMenus(), oldProject.getMenus());
        }
        apiDocService.saveAPIDocProjectDetail(project);
        deleteHistoryData(oldProject);
    }

    public void deleteHistoryData(APIDocProject oldProject) {
        if (oldProject == null) {
            return;
        }

        apiDocService.deleteAPIDocProject(oldProject);
        List<APIDocProject> projects = apiDocService.findProjectDeletedByName(oldProject.getName());
        for (int i = 5; i < projects.size(); i++) {
            apiDocService.dropAPIDocProject(projects.get(i));
        }
    }

    private void mergeMenu(Set<APIDocMenu> menus, Set<APIDocMenu> oldMenus) {
        menus.forEach(menu -> {
            Optional<APIDocMenu> oldMenu = oldMenus.stream().filter(menu::equals).findFirst();
            if (!oldMenu.isPresent()) {
                return;
            }
            menu.setDesc(oldMenu.get().getDesc());
            if (menu.getApiDoc() != null && oldMenu.get().getApiDoc() != null) {
                mergeDoc(menu.getApiDoc(), oldMenu.get().getApiDoc());
            }
            mergeMenu(menu.getChilds(), oldMenu.get().getChilds());
        });
    }

    private void mergeDoc(APIDoc apiDoc, APIDoc oldApiDoc) {
        mergeField(apiDoc.getRequest(), oldApiDoc.getRequest());
        mergeField(apiDoc.getResponse(), oldApiDoc.getResponse());
    }

    private void mergeField(List<APIDocField> fields, List<APIDocField> oldFields) {
        fields.forEach(field -> {
            Optional<APIDocField> oldField = oldFields.stream().filter(field::equals).findFirst();
            if (!oldField.isPresent()) {
                return;
            }
            field.setDesc(oldField.get().getDesc());

            mergeField(field.getChilds(), oldField.get().getChilds());
        });
    }
}
