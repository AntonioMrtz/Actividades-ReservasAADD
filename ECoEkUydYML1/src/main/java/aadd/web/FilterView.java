package aadd.web;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import org.primefaces.util.LangUtils;

import aadd.bean.Activity;
import dao.ActivityDAO;

@Named("dtFilterView")
@ViewScoped
public class FilterView implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@Inject
    private ActivityDAO service;

    private List<Activity> actividades;

    private List<Activity> filteredCustomers1;

    private List<FilterMeta> filterBy;

    private boolean globalFilterOnly;
    
    private String[] tipos;

    @PostConstruct
    public void init() {
        globalFilterOnly = false;
        tipos=getTipos();
       
        actividades=ActivityDAO.getActivityDAO().getAllActivities();

        filterBy = new ArrayList<>();

        filterBy.add(FilterMeta.builder()
                .field("tipo")
                .filterValue(tipos)
                .matchMode(MatchMode.EQUALS)
                .build());

        filterBy.add(FilterMeta.builder()
                .field("date")
                .filterValue(new ArrayList<>(Arrays.asList(LocalDate.now().minusDays(28), LocalDate.now().plusDays(28))))
                .matchMode(MatchMode.BETWEEN)
                .build());

    }

    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);
        
        return true;

        //Customer customer = (Customer) value;
//        return customer.getName().toLowerCase().contains(filterText)
//                || customer.getCountry().getName().toLowerCase().contains(filterText)
//                || customer.getRepresentative().getName().toLowerCase().contains(filterText)
//                || customer.getDate().toString().toLowerCase().contains(filterText)
//                || customer.getStatus().name().toLowerCase().contains(filterText)
//                || customer.getActivity() < filterInt;
    }

    public void toggleGlobalFilter() {
        setGlobalFilterOnly(!isGlobalFilterOnly());
    }

    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }

    public String[] getTipos() {
    	String[] array = new String[]{"	artesania","cine","fotografía","mind&body" }; 
        return array;
    }
    
   

    public ActivityDAO getService() {
		return service;
	}

	public void setService(ActivityDAO service) {
		this.service = service;
	}

	public List<Activity> getActividades() {
		return actividades;
	}

	public void setActividades(List<Activity> actividades) {
		this.actividades = actividades;
	}

	public void setFilterBy(List<FilterMeta> filterBy) {
		this.filterBy = filterBy;
	}

	public void setTipos(String[] tipos) {
		this.tipos = tipos;
	}

	public List<Activity> getFilteredCustomers1() {
        return filteredCustomers1;
    }

    public void setFilteredCustomers1(List<Activity> filteredCustomers1) {
        this.filteredCustomers1 = filteredCustomers1;
    }



    public List<FilterMeta> getFilterBy() {
        return filterBy;
    }

    public boolean isGlobalFilterOnly() {
        return globalFilterOnly;
    }

    public void setGlobalFilterOnly(boolean globalFilterOnly) {
        this.globalFilterOnly = globalFilterOnly;
    }
}