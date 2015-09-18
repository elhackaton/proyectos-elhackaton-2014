<?php

define("SOURCE", "local");
define("OUTPUT", "sql");
  
  switch(SOURCE) {
    // From web
    case "web":
      $html = file_get_contents('http://www.e-aditivos.com');    
      break;

    // From the local copy
    case "local":
      $html = file_get_contents('aditivos/data.html');    
      break;
  }  

  $dom = new DOMDocument();  
  libxml_use_internal_errors(TRUE);
  if(!empty($html)){
    $dom->loadHTML($html);
    libxml_clear_errors();  
    $tables = $dom->getElementsByTagName('table');
    $rows = $tables->item(0)->getElementsByTagName('tr');
      
    foreach ($rows as $row) {
        $node = $row->nodeValue;
        $values = split("\n", $node);
        
        switch (OUTPUT) {
          // Print for debug
          case "debug":
            echo "id: ".trim($values[0])."\n";
            echo "dangerous: ".trim($values[1])."\n";
            echo "name: ".trim($values[2])."\n";
            echo "type: ".trim($values[3])."\n\n";
            break;
          
          // Generate output in .csv format
          case "csv":
            echo trim($values[0]).";".trim($values[1]).";".trim($values[2]).";".trim($values[3]).";\n";
            break;

          // Generate outpout in .sql
          case "sql":
            $sql = "INSERT INTO aditivos (id, dangerous, name, type) VALUES ";
            $sql = $sql . "('".trim($values[0])."','".trim($values[1])."','".trim($values[2])."','".trim($values[3])."');";
            echo $sql;
            break;
        }

    }
  }

?>