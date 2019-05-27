
function getIndex(list, id) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}

var  messageApi = Vue.resource('/devices/control/db{/id}');

Vue.component('device-form', {
    props: ['devices', 'deviceAttr'],
    data: function() {
        return {
            name: '',
            type: '',
            gpio: '',
            creation_date: '',
            id: ''
        }
    },
    watch: {
      deviceAttr: function(newVal, oldVal) {
         this.name = newVal.name;
         this.type = newVal.type;
         this.gpio = newVal.gpio;
         this.creation_date = newVal.creation_date;
         this.id = newVal.id;
      }
    },
    template:
        '<div>'+
            '<input type="text" placeholder=" type " v-model="type" />' +
            '<input type="text" placeholder=" name " v-model="name" />' +
            '<input type="text" placeholder=" gpio " v-model="gpio" />' +
            '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function() {
            var device = {
                gpio: this.gpio,
                type: this.type,
                name: this.name
             };

            if(this.id) {
                messageApi.update({id: this.id}, device).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.devices, data.id);
                        this.devices.splice(index, 1, data);
                        this.name = ''
                        this.type = ''
                        this.gpio = ''
                        this.id = ''
                        this.creation_date = ''
                    })
                )
            } else {
                messageApi.save({}, device).then(result =>
                                result.json().then(data => {
                                    this.devices.push(data);
                                    this.name = ''
                                    this.type = ''
                                    this.gpio = ''
                                })
                            )
            }

        }
    }
});

Vue.component('device-row', {
    props: ['device', 'editMethod', 'devices'],
    template:
        '<div>'+
            '<i>(ID: {{ device.id }})</i>' +
                ' NAME: {{ device.name }}, TYPE: {{ device.type }}, GPIO: {{ device.gpio }}, DATE: {{ device.creation_date }}'+
            '<span style="position: absolute; right: 0">' +
                '<input type="button" value="Edit" @click="edit" />' +
                '<input type="button" value="X" @click="del" />' +
            '</span>'+
        '</div>',
    methods: {
        edit: function() {
            this.editMethod(this.device);
        },

        del: function() {
            messageApi.remove({id: this.device.id}).then(result => {
                if (result.ok) {
                    this.devices.splice(this.devices.indexOf(this.device), 1)
                }
            })
        }
    }
});

Vue.component('devices-list', {
    props: ['devices'],
    data: function() {
        return {
            device: null
        }
    },
    template:
    '<div style="position: relative; width: 600px;">'
        +'<device-form :devices="devices" :deviceAttr="device" />'
        +'<device-row v-for="device in devices" :key="device.id" :device="device" :editMethod="editMethod" :devices="devices" />'+
    '</div>',
    created: function() {
        messageApi.get().then(result =>
            result.json().then(data =>
                data.forEach(device => this.devices.push(device))
            )
        )
    },
    methods: {
        editMethod: function(device) {
            this.device = device;

        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<devices-list :devices="devices" />',
    data: {
        devices: []
    }
});