package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MyController {

	
	@Autowired
	private RestTemplate rTemplate;
	
	@GetMapping("/entries/{category}")
	public ResponseEntity<List<EntryDTO>> getEntryByDescription(@PathVariable("category") String category){
		
		
		DATA data = rTemplate.getForObject("https://api.publicapis.org/entries", DATA.class);
		
		List<Entry> list = data.getEntries();
		
		List<EntryDTO> listDto = new ArrayList<>();
		for(Entry e:list) {
			String[] s = e.getCategory().split(" ");
			if(s.length==1) {
				String[] s1 = s[0].split("-");
				if(s1[0].equals(category)) {
					System.out.println(s1[0]);
					listDto.add(new EntryDTO(e.getApi(),e.getDescription()));
				}
			}else {
			if(s[0].equals(category)) {
//				System.out.println(s[0]);
				listDto.add(new EntryDTO(e.getApi(),e.getDescription()));
			}
			}
		}
		
		return new ResponseEntity<List<EntryDTO>>(listDto,HttpStatus.OK);
		
	}
	
	
	@PostMapping("/entries")
	public ResponseEntity<String> addNewEntry(@RequestBody Entry entry){
		DATA d= rTemplate.getForObject("https://api.publicapis.org/entries", DATA.class);
		
		
		List<Entry> ent= d.getEntries();
		Boolean flag = ent.add(entry);
		if(flag==true) {
			return new ResponseEntity<String>("New Entry is Added Succesfully..",HttpStatus.ACCEPTED);
		}else {
			return new ResponseEntity<String>("New Entry is not added",HttpStatus.ACCEPTED);
		}
	}
}
