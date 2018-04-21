import moment from 'moment';
import 'moment-timezone';

export default {
    methods: {
        utcToChicago: function(utcDateTime) {
            return moment.tz(utcDateTime, "UTC").clone().tz("America/Chicago").format();
        },
        host: function() {
            return process.env.NODE_ENV === 'production' ? 'gcloud.weizilla.com' : 'http://localhost:8080';
        }
    }
}
