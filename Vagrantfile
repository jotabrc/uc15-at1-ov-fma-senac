# -*- mode: ruby -*-
# vi: set ft=ruby  :

machines = {
    "mysql" => {"memory" => "1024", "cpu" => "1", "ip" => "101", "image" => "bento/ubuntu-24.04"}
}

Vagrant.configure("2") do |config|
    machines.each do |name, config|
        config.vm.define "#{name}" do |machine|
            machine.vm.box = "#{conf["image"]}"
            machine.vm.hostname = "#{name}"
            machine.vm.network "private_network" ip: "192.168.56.#{conf["ip"]}"
            machine.vm.provider "virtualbox" do |vb|
                vb.name = "#{name}"
                vb.memory = conf["memory"]
                vm.cpus = conf["cpu"]
            end
        end
    end
end