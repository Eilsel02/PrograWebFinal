package consulado.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import consulado.entities.Distrito;
import consulado.entities.Local;
import consulado.services.DistritoService;
import consulado.services.LocalService;

@Controller
@RequestMapping("/locales")
public class LocalController {
	@Autowired
    private DistritoService distritoService;
	@Autowired
    private LocalService localService;
	
    @GetMapping("/c-list-local")
    public String showListaLocales(Model model){

    	model.addAttribute("listLocal", localService.listAll());
        return "/locales/list-local";
    }
    

    @GetMapping("/c-add-local")
    public String showNuevoLocal(Model model){
		model.addAttribute("local", new Local());
        return "/locales/add-local";
    }
    
    
    @PostMapping("/do-add-local")
    public String doNuevoLocal(@Valid Local local, BindingResult result, Model model){
    	 if (result.hasErrors()) {
             return "/locales/add-local";
         }
         
    	 localService.save(local);

    	 model.addAttribute("listLocal", localService.listAll());
         return "/locales/list-local";
    }
    
    
    @GetMapping("/c-edit-local/{id}")
    public String showEditarLocal(@PathVariable("id") long id, Model model) {
        Local local = localService.findById(id);
        model.addAttribute("local", local);
        model.addAttribute("distrito", new Distrito());
        model.addAttribute("listDistrito_por_asignar", distritoService.findByLocalIdNull());
        model.addAttribute("listDistrito_asignado", distritoService.findByLocalId(id));
        return "/locales/edit-local";
    }
    
    @PostMapping("/do-edit-local")
    public String doEditLocal(@Valid Local local, BindingResult result, Model model){
    	 if (result.hasErrors()) {
             return "/locales/edit-local";
         }
         
    	 localService.save(local);

    	 model.addAttribute("listLocal", localService.listAll());
         return "/locales/list-local";
    }
    
    
    @PostMapping("/do-asociar-local-distrito")
    public String doAsociarLocal(@Valid Distrito distrito, Long idlocal,BindingResult result, Model model){
		
 
    	distritoService.asociaDistritoConLocal(distrito.getId(),idlocal);
    	
    	Local local = localService.findById(idlocal);
        model.addAttribute("local", local);        
        model.addAttribute("distrito", new Distrito());        
        model.addAttribute("listDistrito_por_asignar", distritoService.findByLocalIdNull());        
        model.addAttribute("listDistrito_asignado", distritoService.findByLocalId(local.getId()));        
        
        return "/locales/edit-local";
    }
    
    
    
    @GetMapping("/do-desasociar-local-distrito/{id}")
    public String doDesasociarLocal(@PathVariable("id") long id, Model model){
		
    	long idlocal=distritoService.findById(id).getLocal().getId();
    	distritoService.desasociaDistritoDeLocal(id);
    	
    	//throw new Exception("Exception message");
    	
    	Local local = localService.findById(idlocal);
        model.addAttribute("local", local);        
        model.addAttribute("distrito", new Distrito());        
        model.addAttribute("listDistrito_por_asignar", distritoService.findByLocalIdNull());        
        model.addAttribute("listDistrito_asignado", distritoService.findByLocalId(idlocal));        
        
        return "/locales/edit-local";
    }
    
    
    @GetMapping("/do-delete-local/{id}")
    public String doEliminarLocal(@PathVariable("id") long id, Model model) {
        Local local = localService.findById(id);
        localService.delete(local);
        
   	 model.addAttribute("listLocal", localService.listAll());
        return "/locales/list-local";
    }
    
    
}
