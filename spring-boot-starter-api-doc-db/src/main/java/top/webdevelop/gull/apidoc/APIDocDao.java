package top.webdevelop.gull.apidoc;

import org.springframework.beans.BeanWrapper;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import top.webdevelop.gull.autoconfigure.APIDocJdbcTemplate;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Optional;

/**
 * Created by xumingming on 2018/6/13.
 */
public class APIDocDao {
    private static final String ROOT_ID = "0";

    private JdbcTemplate jdbcTemplate;

    public APIDocDao() {
        this.jdbcTemplate = APIDocJdbcTemplate.jdbcTemplate();
    }

    public APIDocDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteProject(APIDocProject project) {
        jdbcTemplate.update("update api_doc_project set deleted = true where id = ?", project.getId());
    }

    public void deleteMenuByProject(APIDocProject project) {
        jdbcTemplate.update("update api_doc_menu set deleted = true where api_doc_project_id = ?", project.getId());
    }

    public void deleteDocByProject(APIDocProject project) {
        jdbcTemplate.update("update api_doc set deleted = true where api_doc_project_id = ?", project.getId());
    }

    public void deleteFieldByProject(APIDocProject project) {
        jdbcTemplate.update("update api_doc_field set deleted = true where api_doc_project_id = ?", project.getId());
    }

    public void dropProject(APIDocProject project) {
        jdbcTemplate.update("delete from api_doc_project where id = ?", project.getId());
    }

    public void dropMenuByProject(APIDocProject project) {
        jdbcTemplate.update("delete from api_doc_menu where api_doc_project_id = ?", project.getId());
    }

    public void dropDocByProject(APIDocProject project) {
        jdbcTemplate.update("delete from api_doc where api_doc_project_id = ?", project.getId());
    }

    public void dropFieldByProject(APIDocProject project) {
        jdbcTemplate.update("delete from api_doc_field where api_doc_project_id = ?", project.getId());
    }

    public int insertProject(APIDocProject project) {
        return jdbcTemplate.update("insert into api_doc_project(id, name) values(?, ?)", project.getId(), project.getName());
    }

    public int insertMenu(APIDocProject project, APIDocMenu parent, APIDocMenu menu) {
        return jdbcTemplate.update("insert into api_doc_menu(id, api_doc_project_id, parent_id, `desc`,mapping, action, api_doc_id) values(?, ?, ?, ?, ?, ?, ?)",
                menu.getId(), project.getId(), Optional.ofNullable(parent).map(APIDocMenu::getId).orElse(ROOT_ID), menu.getDesc(), menu.getMapping(), menu.getAction(), Optional.ofNullable(menu.getApiDocId()).orElse(null));
    }

    public int insertDoc(APIDocProject project, APIDoc doc) {
        return jdbcTemplate.update("insert into api_doc(id, api_doc_project_id, url, action, contact) values(?, ?, ?, ?, ?)", doc.getId(), project.getId(), doc.getUrl(), doc.getAction(), doc.getContact());
    }

    public int insertField(APIDocProject project, APIDoc doc, APIDocField parent, APIDocField field, APIDocFieldParamsType paramsType) {
        return jdbcTemplate.update("insert into api_doc_field(id, api_doc_project_id, api_doc_id, parent_id, name, type, required, `desc`,params_type) values(?, ?, ?, ?, ? ,?, ?, ?, ?)", field.getId(), project.getId(), doc.getId(), Optional.ofNullable(parent).map(APIDocField::getId).orElse(ROOT_ID), field.getName(), field.getType().name(), field.isRequired(), field.getDesc(), paramsType.name());
    }

    public int updateMenuDesc(String menuId, String desc, Long version) {
        return jdbcTemplate.update("update api_doc_menu set `desc` = ?, version = version + 1 where id = ? and version = ?", desc, menuId, version);
    }

    public int updateFieldDesc(String fieldId, String desc, Long version) {
        return jdbcTemplate.update("update api_doc_field set `desc` = ?, version = version + 1 where id = ? and version = ?", desc, fieldId, version);
    }

    public List<APIDocProject> findAllProject() {
        return jdbcTemplate.query("select * from api_doc_project where deleted = false order by name", new BeanPropertyRowMapper<>(APIDocProject.class));
    }

    public List<APIDocProject> findProjectDeletedByName(String name) {
        return jdbcTemplate.query("select * from api_doc_project where name = ? and deleted = true order by create_date_time desc", new Object[]{name}, new BeanPropertyRowMapper<>(APIDocProject.class));
    }

    public APIDocProject findProjectByName(String name) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("select * from api_doc_project where name = ? and deleted = false", new Object[]{name}, new BeanPropertyRowMapper<>(APIDocProject.class)));
    }

    public List<APIDocMenu> findAllAPIDocMenuByProjectAndParent(APIDocProject project, APIDocMenu parent) {
        return jdbcTemplate.query("select * from api_doc_menu where api_doc_project_id = ? and parent_id = ? and deleted = false", new Object[]{project.getId(), Optional.ofNullable(parent).map(APIDocMenu::getId).orElse(ROOT_ID)}, new BeanPropertyRowMapper<>(APIDocMenu.class));
    }

    public APIDoc findAPIDocById(String id) {
        return DataAccessUtils.singleResult(jdbcTemplate.query("select * from api_doc where id = ? and deleted = false", new Object[]{id}, new BeanPropertyRowMapper<>(APIDoc.class)));
    }

    public List<APIDocField> findAllAPIDocFieldByDocAndParentAndParamsType(APIDoc apiDoc, APIDocField parent, APIDocFieldParamsType paramsType) {
        return jdbcTemplate.query("select * from api_doc_field where api_doc_id = ? and parent_id = ? and params_type = ? and deleted = false", new Object[]{apiDoc.getId(), Optional.ofNullable(parent).map(APIDocField::getId).orElse(ROOT_ID), paramsType.name()}, new APIDocFieldBeanPropertyRowMapper<>(APIDocField.class));
    }


    public static class APIDocFieldTypeEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(final String text) throws IllegalArgumentException {
            setValue(APIDocFieldType.valueOf(text));
        }

        @Override
        public void setValue(final Object value) {
            super.setValue(APIDocFieldType.valueOf(value.toString()));
        }

        @Override
        public APIDocFieldType getValue() {
            return (APIDocFieldType) super.getValue();
        }

        @Override
        public String getAsText() {
            return getValue().name();
        }
    }

    public static class APIDocFieldBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
        public APIDocFieldBeanPropertyRowMapper(Class<T> mappedClass) {
            super(mappedClass);
        }

        @Override
        protected void initBeanWrapper(BeanWrapper bw) {
            super.initBeanWrapper(bw);
            bw.registerCustomEditor(APIDocFieldType.class, "type", new APIDocFieldTypeEditor());
        }
    }
}
