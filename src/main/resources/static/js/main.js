
function getIndex(list, id) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}

var  messageApi = Vue.resource('/devices/control/db{/id}');

Vue.component('control-form', {
    props: ['controls', 'deviceAttr'],
    data: function() {
        return {
            name: '',
            gpio_number: '',
            creation_date: '',
            signal: '',
            pwm_status: '',
            id: ''
        }
    },
    watch: {
      deviceAttr: function(newVal, oldVal) {
         this.name = newVal.name;
         this.gpio_number = newVal.gpio_number;
         this.creation_date = newVal.creation_date;
         this.signal = newVal.signal;
         this.pwm_status = newVal.pwm_status;
         this.id = newVal.id;
      }
    },
    template:
        '<div>'+
            '<input type="text" placeholder=" name " v-model="name" />' +
            '<input type="text" placeholder=" gpio_number " v-model="gpio_number" />' +
            '<input type="text" placeholder=" signal " v-model="signal" />' +
            '<input type="text" placeholder=" pwm_status " v-model="pwm_status" />' +
            '<input type="button" value="Save" @click="save" />' +
        '</div>',
    methods: {
        save: function() {
            var control = {
                gpio_number: this.gpio_number,
                signal: this.signal,
                pwm_status: this.pwm_status,
                name: this.name
             };

            if(this.id) {
                messageApi.update({id: this.id}, control).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.controls, data.id);
                        this.controls.splice(index, 1, data);
                        this.name = ''
                        this.signal = ''
                        this.pwm_status = ''
                        this.gpio_number = ''
                        this.id = ''
                        this.creation_date = ''
                    })
                )
            } else {
                messageApi.save({}, control).then(result =>
                                result.json().then(data => {
                                    this.controls.push(data);
                                    this.name = ''
                                    this.pwm_status = ''
                                    this.signal = ''
                                    this.gpio_number = ''
                                })
                            )
            }

        }
    }
});

Vue.component('control-row', {
    props: ['control', 'editMethod', 'controls'],
    template:
        '<div>'+
            '<i>(ID: {{ control.id }})</i>' +
                ' NAME: {{ control.name }}, GPIO_NUMBER : {{ control.gpio_number }}, SIGNAL : {{ control.signal }}, PWM : {{ control.pwm_status }}, DATE: {{ control.creation_date }}'+
            '<span style="position: absolute; right: 0">' +
                '<input type="button" value="Edit" @click="edit" />' +
                '<input type="button" value="X" @click="del" />' +
            '</span>'+
        '</div>',
    methods: {
        edit: function() {
            this.editMethod(this.control);
        },

        del: function() {
            messageApi.remove({id: this.control.id}).then(result => {
                if (result.ok) {
                    this.controls.splice(this.controls.indexOf(this.control), 1)
                }
            })
        }
    }
});

Vue.component('controls-list', {
    props: ['controls'],
    data: function() {
        return {
            control: null
        }
    },
    template:
    '<div style="position: relative; width: 600px;">'
        +'<control-form :controls="controls" :deviceAttr="control" />'
        +'<control-row v-for="control in controls" :key="control.id" :control="control" :editMethod="editMethod" :controls="controls" />'+
    '</div>',
    created: function() {
        messageApi.get().then(result =>
            result.json().then(data =>
                data.forEach(control => this.controls.push(control))
            )
        )
    },
    methods: {
        editMethod: function(control) {
            this.control = control;

        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<controls-list :controls="controls" />',
    data: {
        controls: []
    }
});