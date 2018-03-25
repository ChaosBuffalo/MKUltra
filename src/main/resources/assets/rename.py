import os
import json

def do_dict_iter(dict_data):
    new_map = {}
    for key, value in dict_data.iteritems():
        if isinstance(value, dict):
            new_map[key] = do_dict_iter(value)
        elif isinstance(value, unicode):
            original_value = value
            value = value.lower()
            if value.startswith(u"minekampf:"):
                new_map[key] = value.replace(u"minekampf", u"mkultra")
            else:
                new_map[key] = value
            # print("is string", key, new_map[key], original_value)
        else:
            new_map[key] = value
    return new_map


OUT_DIR = "mkultra_redo"
IN_DIR = "mkultra"


for root, dirs, files in os.walk(IN_DIR):
    for name in files:
        new_loc = os.path.join(root.replace(IN_DIR, OUT_DIR))
        if not (os.path.exists(new_loc)):
            os.makedirs(new_loc)

        print(root, dirs, name)
        if name.endswith(".json"):
            with open(os.path.join(root, name)) as data_file:
                json_data = json.load(data_file)
                new_json = do_dict_iter(json_data)
                with open(os.path.join(new_loc, name), 'w') as outfile:
                    json.dump(new_json, outfile)

