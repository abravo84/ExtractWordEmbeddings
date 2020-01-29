'''
Created on Nov 10, 2017

@author: upf
'''
import sys, os


            
def add_one_dictionary(dictionary, key):
    if key in dictionary:
        aux = dictionary.get(key)
        dictionary[key] = aux + 1
    else:
        dictionary[key] = 1
    return dictionary          


    
def create_window_format(file_path, output_path, window, ref_file, feats, filename=""):
    
    print filename
    
    iden_list = []
    for lin in file(ref_file):
        iden = lin.strip().replace(filename,"#").split(",")[0]
        print iden
        iden_list.append(iden.replace("#", filename))
        print iden_list[-1]
        
        
    olab = open(output_path, "w")
    nlines = {}
    for lin in file(file_path):
        fields = lin.strip().replace(filename,"#").replace("NaN","0.0").split(",")
        
        key = fields[0]
        if len(filename):
            key = fields[0].replace("#", filename)
        
        nlines[key] = ",".join(fields[1:]).replace("#", filename)
        print "sdad", len(fields[1:])
    
    new_lines = {}
    num_lines = {}
    for doc_id_sent in sorted(nlines):
        doc_id = "-".join(doc_id_sent.split("_PreProcessed_gate")[0])
        doc_id = doc_id_sent.split("-")[0]
        line = new_lines.get(doc_id, [])
        line.append(nlines[doc_id_sent])
        new_lines[doc_id] = line
        add_one_dictionary(num_lines, doc_id)
    
    count = 0
    for doc_id in sorted(new_lines):
        
        nlines = new_lines[doc_id]
        i=0
        
        padding_line = ",".join(["0.0"]*(feats))
        padding_list = [padding_line]*window
        while i < len(nlines):
            
            before = nlines[i-window:i]
            if i < window:
                before = nlines[:i]
                before = padding_list + before
                before = before[-window:]
            
            middle = [nlines[i]]
            
            after = nlines[i+1:i+1+window]
            if i +window >= len(nlines):
                after = after + padding_list
                after = after[:window]
            
            
            total = before + middle + after
            print "total", len(total)
            
            olab.write(iden_list[count].replace(",", "-") + "," + ",".join(total) + "\n")
            olab.flush()
            count+=1 
            i+=1
           
    olab.close() 
  
  
if __name__ == '__main__':

    file_path = "/data/aaburaed/TAGRWS/missing/features"
    output_path = "/data/abravo/corpora/ahmed/missing"
    nfeatures = 29
    nwindow = 3


    
    for filename in sorted(os.listdir(file_path)):
        
        if "add_feat_vec_max" in filename and not "_window_" in filename:
            nfile_path = file_path + "/" + filename
            noutput_path = output_path + "/" + filename.replace("vec_max", "vec_max_window").replace(",", "-").replace("_PreProcessed_gate.xml.csv", "")
            ref_file = nfile_path
            
            filename = filename.replace("add_feat_vec_max_", "").replace("_PreProcessed_gate.xml.csv", "")
            
            create_window_format(nfile_path, noutput_path + ".csv", nwindow, ref_file, nfeatures, filename)
