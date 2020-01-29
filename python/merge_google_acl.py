'''
Created on Jan 29, 2018

@author: upf
'''
import os, sys
from shutil import copyfile

def get_lines(path):
    
    res = []
    
    for lin in file(path):
        res.append(lin.strip())
    
    return res

def merge_file_testing(test_path, acl_prefix, google_prefix, output_prefix):
    
    doc_id_done={}
    for filename in sorted(os.listdir(test_path)):
        
        if not filename.startswith(acl_prefix):
            continue
        
        doc_id = filename.split("_")[-1]
        
        if doc_id in doc_id_done:
            continue
        
        doc_id_done[doc_id] = 1
        
        acl_path = os.path.join(test_path,acl_prefix + doc_id)
        google_path = os.path.join(test_path,google_prefix + doc_id)
        
        acl_list = get_lines(acl_path)
        google_list = get_lines(google_path)
        
        print len(acl_list), len(google_list)
        
        output_path=  os.path.join(test_path,output_prefix + doc_id)
        
        ofile = open(output_path, "w")
        
        i=0
        
        while (i < len(acl_list)):
            nline =  google_list[i] + "," +  ",".join(acl_list[i].split(",")[1:])
            print len(nline.split(","))
            i+=1
            ofile.write(nline + "\n")
        
        
        ofile.close()

def merge_file_training(google_path, acl_path, output_path):
    acl_list = get_lines(acl_path)
    google_list = get_lines(google_path)
    
    print len(acl_list), len(google_list)
    
    ofile = open(output_path, "w")
    
    i=0
    
    while (i < len(acl_list)):
        nline =  google_list[i] + "," +  ",".join(acl_list[i].split(",")[1:])
        print len(nline.split(","))
        i+=1
        ofile.write(nline + "\n")
    
    
    ofile.close()
    
    
def copy_files(input_folder, output_folder):
    
    for filename in sorted(os.listdir(input_folder)):
        
        if not filename.endswith(".csv"):
            continue
        
        new_filename = filename.replace("_PreProcessed_gate.xml", "").replace(",", "-")
        
        
        src = os.path.join(input_folder, filename)
        dst = os.path.join(output_folder, new_filename)
        copyfile(src, dst)
        
    
    
def count_lines_for_each_folder(folder):
    
    for filename in sorted(os.listdir(folder)):
        
        print filename, len(open(folder + "/" + filename).readlines())

    
if __name__ == '__main__':
    
    
    copy_files("//home/abravo/ahmed/feats", "/home/abravo/ahmed/feats/ahmed")
    
    sys.exit()
    
    test_path = "/home/abravo/ahmed/"
    
    acl_prefix = "acl_vec_sw_15_"
    google_prefix = "google_vec_sw_15_"
    output_prefix = "googleacl_vec_sw_15_"
    merge_file_testing(test_path, acl_prefix, google_prefix, output_prefix)
    
    
    sys.exit()
    
    
    
    folder = "/home/upf/corpora/SciSumm-2018/training/features_mix"
    count_lines_for_each_folder(folder)
    sys.exit()
    #merge 2017 and 2018
    
    path_2017 = "/home/upf/corpora/SciSumm-2018/training/features"
    path_2018 = "/home/upf/corpora/SciSumm-2018/training/features_2018"
    path_mix = "/home/upf/corpora/SciSumm-2018/training/features_mix"
    
    for filename in sorted(os.listdir(path_2017)):
        
        print filename
        
        ofile= open(path_mix + "/" + filename, "w")
        
        for lin in file(path_2017+ "/" + filename):
            ofile.write(lin)
        
        for lin in file(path_2018+ "/" + filename):
            ofile.write(lin)
        
        ofile.close()
    
    sys.exit()
    
    acl_path = "/home/upf/corpora/SciSumm-2018/training/features_2018/acl_vec_sw_15.txt"
    google_path = "/home/upf/corpora/SciSumm-2018/training/features_2018/google_vec_sw_15.txt"
    output_path = "/home/upf/corpora/SciSumm-2018/training/features_2018/googleacl_vec_sw_15.txt"
    
    
    #merge_file_training(google_path, acl_path, output_path)
    
    
    
    test_path = "/home/upf/corpora/SciSumm-2018/testing/features"
    
    acl_prefix = "acl_vec_sw_15_"
    google_prefix = "google_vec_sw_15_"
    output_prefix = "googleacl_vec_sw_15_"
    merge_file_testing(test_path, acl_prefix, google_prefix, output_prefix)
    
    
    sys.exit()
    
    acl_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/acl_vec_15.txt"
    google_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/google_vec_15.txt"
    output_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/googleacl_vec_15.txt"
    
    
    #merge_file_training(google_path, acl_path, output_path)
    
    acl_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/acl_vec_15_tf_idf.txt"
    google_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/google_vec_15_tf_idf.txt"
    output_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/googleacl_vec_15_tf_idf.txt"
    
    #merge_file_training(google_path, acl_path, output_path)
    
    
    acl_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/acl_vec_sw_10.txt"
    google_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/google_vec_sw_10.txt"
    output_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/googleacl_vec_sw_10.txt"
    
    #merge_file_training(google_path, acl_path, output_path)
    
    
    acl_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/acl_vec_sw_15.txt"
    google_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/google_vec_sw_15.txt"
    output_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/googleacl_vec_sw_15.txt"
    
    #merge_file_training(google_path, acl_path, output_path)
    
    
    
    
    
    
    
    
    test_path = "/home/upf/corpora/SciSUM-2017-arffs-and-models/test_sets_ab/ALL-DOCS-2016-FEATURES/"
    
    acl_prefix = "acl_vec_15_"
    google_prefix = "google_vec_15_"
    output_prefix = "googleacl_vec_15_"
    merge_file_testing(test_path, acl_prefix, google_prefix, output_prefix)
    
    acl_prefix = "acl_vec_15_tf_idf_"
    google_prefix = "google_vec_15_tf_idf_"
    output_prefix = "googleacl_vec_15_tf_idf_"
    merge_file_testing(test_path, acl_prefix, google_prefix, output_prefix)
    
    acl_prefix = "acl_vec_sw_10_"
    google_prefix = "google_vec_sw_10_"
    output_prefix = "googleacl_vec_sw_10_"
    merge_file_testing(test_path, acl_prefix, google_prefix, output_prefix)
    
    acl_prefix = "acl_vec_sw_15_"
    google_prefix = "google_vec_sw_15_"
    output_prefix = "googleacl_vec_sw_15_"
    merge_file_testing(test_path, acl_prefix, google_prefix, output_prefix)
    
    
    
    
    
    
    
    
    
    
    
    
    
    sys.exit()
    
    labels_folder = "/home/upf/corpora/SciSUM-2017-arffs-and-models/training_sets_ab/ALL-DOCS-2016-FEATURES/"
    
    summ_type = "abstract"
    
    
    
    labels_dict = {}
    for fname in sorted(os.listdir(labels_folder)):
        
        if not fname.startswith("labels_"):
            continue
        if "_class" in fname:
            continue
        if not summ_type in fname:
            continue
        
        fname_path = os.path.join(labels_folder, fname)
        
        print fname_path
        
        for lin in file(fname_path):
            fields = lin.strip().split(",")
            labels = labels_dict.get(fields[0], "")
            labels += ","+fields[1]
            
            labels_dict[fields[0]]=labels
        
    ofile = open(labels_folder+"labels_all.csv", "w")    
    for k in sorted(labels_dict):
        ofile.write(k+labels_dict[k]+"\n")
    ofile.close()
    
    