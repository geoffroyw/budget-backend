class BankAccountSerializer < ActiveModel::Serializer
  attributes :id, :name, :currency_code
  has_many :transactions
end
