#!/usr/bin/env lua


function check_superuser()
    tmp = assert(io.popen('id -u', 'r'))
    current_user = assert(tmp:read('*a'))
    tmp:close()
    current_user = string.gsub(current_user, "\n", "")
    root_user = '0'

    if current_user ~= root_user then
        print('Please execute this script as root user.')
        os.exit()
    end
end


function install_dependencies()
    os.execute('apt install -y python3')
end


function install_launcher()
    source_dir = arg[0]:match("(.*/)") or "./"
    destination_dir = '/opt/launcher'

    os.execute('mkdir -p ' .. destination_dir)
    os.execute('cp ' .. source_dir .. 'index.html ' .. destination_dir .. '/index.html')
end


function generate_launcher()
    destination_dir = '/opt/launcher/start'

    launcher = io.open(destination_dir, 'w')
    launcher:write([[
#!/bin/sh
cd /opt/launcher
python3 -m http.server 80
]])
    launcher:close()

    os.execute('chmod 755 ' .. destination_dir)
end


---------------------------------------------------------------------------------------------------


check_superuser()

install_dependencies()
install_launcher()
generate_launcher()

print("")
print("Launcher installed.")
print("To start it please run command: sudo /opt/launcher/start")
print("Then open your device address in a browser.")
