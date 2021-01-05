package me.ANONIMUS.proxy.protocol.objects;

import java.util.*;

public class GameProfile {
    private final UUID id;
    private final String name;
    private List<Property> properties;
    private Map<TextureType, Texture> textures;
    
    public GameProfile(final String id, final String name) {
        this((id == null || id.equals("")) ? null : UUID.fromString(id), name);
    }
    
    public GameProfile(final UUID id, final String name) {
        if (id == null && (name == null || name.equals(""))) {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        }
        this.id = id;
        this.name = name;
    }

    public GameProfile(final String name, final UUID id) {
        if (id == null && (name == null || name.equals(""))) {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        }
        this.id = id;
        this.name = name;
    }
    
    public boolean isComplete() {
        return this.id != null && this.name != null && !this.name.equals("");
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public String getIdAsString() {
        return (this.id != null) ? this.id.toString() : "";
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Property> getProperties() {
        if (this.properties == null) {
            this.properties = new ArrayList<Property>();
        }
        return this.properties;
    }

    public Property getProperty(final String name) {
        for (final Property property : this.getProperties()) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public Map<TextureType, Texture> getTextures() {
        if (this.textures == null) {
            this.textures = new HashMap<TextureType, Texture>();
        }
        return this.textures;
    }

    public Texture getTexture(final TextureType type) {
        return this.getTextures().get(type);
    }
    
    public static class Property {
        private String name;
        private String value;
        private String signature;
        
        public Property(final String name, final String value) {
            this(name, value, null);
        }
        
        public Property(final String name, final String value, final String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public String getSignature() {
            return this.signature;
        }
        
        public boolean hasSignature() {
            return this.signature != null;
        }
        
        @Override
        public String toString() {
            return "Property{name=" + this.name + ", value=" + this.value + ", signature=" + this.signature + "}";
        }
    }
    
    public enum TextureType {
        SKIN, 
        CAPE;
    }
    
    public enum TextureModel {
        NORMAL, 
        SLIM;
    }
    
    public static class Texture {
        private String url;
        private Map<String, String> metadata;
        
        public Texture(final String url, final Map<String, String> metadata) {
            this.url = url;
            this.metadata = metadata;
        }
        
        public String getURL() {
            return this.url;
        }
        
        public TextureModel getModel() {
            final String model = (this.metadata != null) ? this.metadata.get("model") : null;
            return (model != null && model.equals("slim")) ? TextureModel.SLIM : TextureModel.NORMAL;
        }
    }
}
